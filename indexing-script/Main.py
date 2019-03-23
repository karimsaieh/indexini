from SparkUtils import SparkUtils
from FileProcessor import FileProcessor
from SparkProcessor import SparkProcessor
from FileIndexRepository import FileIndexRepository
from LdaTopicsDescriptionRepository import LdaTopicsDescriptionRepository

import time
start = time. time()

file_processor = FileProcessor()
spark_processor = SparkProcessor()
file_index_repository = FileIndexRepository()
lda_topics_description_repository = LdaTopicsDescriptionRepository()
spark_utils = SparkUtils("local", "indexing-script-app")

files_rdd = spark_utils.read_files("data/*/*")
files_rdd = files_rdd.map(lambda file: file_processor.process(file))

try:
    # Machine Learning
    files_df = spark_utils.rdd_to_df(files_rdd,
                                     ["url", "file_name", "timestamp", "uuid", "words", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type"]).cache()

    files_df_ready_for_ml = files_df.select("url", "words")

    kmeans_df, bisecting_kmeans_df, (lda_with_count_vectorizer_df, topics_descriptions) = \
        spark_processor.process(files_df_ready_for_ml)

    result_df = spark_utils \
        .join_df(files_df, bisecting_kmeans_df, "url",
                 ["url", "file_name", "timestamp", "uuid", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type"], ["bisecting_kmeans_prediction"])
    result_df = spark_utils \
        .join_df(result_df, kmeans_df, "url",
                 ["url", "file_name", "timestamp", "uuid", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type", "bisecting_kmeans_prediction"],
                 ["kmeans_prediction"])
    result_df = spark_utils \
        .join_df(result_df, lda_with_count_vectorizer_df, "url",
                 ["url", "file_name", "timestamp", "uuid", "pre_processed_text", "summary", "most_common", "thumbnail", "content_type", "kmeans_prediction", "bisecting_kmeans_prediction"],
                 ["lda_topics"])


    result_df.show(n=200)
    # print(topics_descriptions)

    # saving lda topics should be before saving index files, cause they might be unavailable for ui
    lda_topics_description_repository.save_lda_topics(topics_descriptions)
    # save dataframe to elasticsearch
    file_index_repository.save_df(result_df)

    #stats rdd
    file_types_stats_df = files_df.groupBy("content_type").count()
    file_types_stats_df.show(n=220) # 2nd column is named "count"
    # TODO: save stats to elasticsearch

except ValueError as e:
    s = str(e)
    if s == "RDD is empty":
        print("RDD is empty & do nothing ?")

end = time. time()
print(end-start)