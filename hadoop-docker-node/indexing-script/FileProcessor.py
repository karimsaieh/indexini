from TextPreProcessor import TextPreProcessor
from Parser import Parser
from ThumbnailGenerator import ThumbnailGenerator
from TextSummarizer import TextSummarizer
from TextMostCommonWordsExtractor import TextMostCommonWordsExtractor
from FileUrlProcessor import FileUrlProcessor
import nltk
import spacy


class FileProcessor:
    def __init__(self):
        # kim: this code runs in the executor
        self.parser = Parser()
        self.text_pre_processor = TextPreProcessor()
        self.thumbnail_generator = ThumbnailGenerator()
        self.text_summarizer = TextSummarizer()
        self.text_most_common_words_extractor = TextMostCommonWordsExtractor()
        self.file_url_processor = FileUrlProcessor()

    def process(self, file):
        # necessary dependencies for the executor
        nltk.download('punkt')
        nltk.download('wordnet')
        # spacy loaded for each row which is heavy,
        # use for each partition instead ?
        nlp_spacy = spacy.load("fr")
        nlp_spacy.max_length = 3000000

        url = file[0]
        binary = file[1]
        print("file path -> " + url)
        content, content_type = self.parser.parse_file(binary)

        thumbnail = self.thumbnail_generator.get_thumbnail(binary, content_type)

        words, preprocessed_text, content = self.text_pre_processor.preprocess_text(content, nlp_spacy)

        summary = self.text_summarizer.get_summary(content,nlp_spacy)

        most_common = self.text_most_common_words_extractor.get_most_common_words(words)

        timestamp, uuid, file_name = self.file_url_processor.get_timestamp_and_uuid(url)

        url = self.file_url_processor.normalizeUrl(url)

        return url, file_name, timestamp, uuid, words, preprocessed_text, summary, most_common, thumbnail, content_type,content

