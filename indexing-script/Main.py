from SparkUtils import SparkUtils
from TextPreProcessor import TextPreProcessor
from Parser import Parser
from SparkProcessor import SparkProcessor
from ThumbnailGenerator import ThumbnailGenerator
from TextSummarizer import TextSummarizer
from TextMostCommonWordsExtractor import TextMostCommonWordsExtractor

parser = Parser()
text_pre_processor = TextPreProcessor()
spark_processor = SparkProcessor()
thumbnail_generator = ThumbnailGenerator()
text_summarizer = TextSummarizer()
text_most_common_words_extractor = TextMostCommonWordsExtractor()
spark_utils = SparkUtils("local", "indexing-script-app")

# using rdd gives better performances than DF, even though it's a bit messy
# TO READ FROM HDFS: hdfs://localhost/pfe/data/save/1552298224747/d12bd6b1-1d67-46e9-9491-adb0e9dca818/*
files_rdd = spark_utils.read_files("data/*/*")

# in:         url, binary
# out:        url, binary, content, content_type
files_rdd_parsed = files_rdd.map(lambda file: parser.parse_file(file)).cache()

# in:         url, binary, content, content_type
# out:        url, content,thumbnail, content_type
files_rdd_with_thumbnail = files_rdd_parsed.map(lambda file: thumbnail_generator.get_thumbnail(file))

# in:         url, content, thumbnail, content_type
# out:        url, content, summary, thumbnail, content_type
files_rdd_with_summary = files_rdd_with_thumbnail.map(lambda file: text_summarizer.get_summary(file))

# in:         url, content, summary, thumbnail, content_type
# out:        url, words, preprocessed_text, summary, thumbnail, content_type
files_rdd_pre_processed = files_rdd_with_summary.map(lambda file: text_pre_processor.preprocess_text(file)).cache()

# in:         url, words, preprocessed_text, summary, thumbnail, content_type
# out:        url, words, preprocessed_text, summary, most_common, thumbnail, content_type
files_rdd_with_most_common_words = files_rdd_pre_processed.map(lambda file: text_most_common_words_extractor.get_most_common_words(file))

try:
    # Machine Learning
    files_df = spark_utils.rdd_to_df(files_rdd_with_most_common_words,
                                     ["url", "words", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type"]).cache()

    files_df_ready_for_ml = files_df.select("url", "words")

    kmeans_df, bisecting_kmeans_df, (lda_with_count_vectorizer_df, topics_descriptions) = \
        spark_processor.process(files_df_ready_for_ml)

    result_df = spark_utils \
        .join_df(files_df, bisecting_kmeans_df, "url",
                 ["url", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type"], ["bisecting_kmeans_prediction"])
    result_df = spark_utils \
        .join_df(result_df, kmeans_df, "url",
                 ["url", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type", "bisecting_kmeans_prediction"],
                 ["kmeans_prediction"])
    result_df = spark_utils \
        .join_df(result_df, lda_with_count_vectorizer_df, "url",
                 ["url", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type", "kmeans_prediction", "bisecting_kmeans_prediction"],
                 ["lda_topics"])


    result_df.show(n=200)
    print(topics_descriptions)


    #TODO: correspondence entre spark & elastic serach in term of attributes
    #TODO: rename column names in camel case ? hmmmmmmmm , gotta see elasticsearch convcentions (i think it's camel case cause json),
    #  or find a way with eshadoop to rename, (  rabbitmq ???)
    # TODO: rename pre_processed_text with text

except ValueError as e:
    s = str(e)
    if s == "RDD is empty":
        print("RDD is empty & do nothing ?")
