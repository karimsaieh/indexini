from TextPreProcessor import TextPreProcessor
from Parser import Parser
from ThumbnailGenerator import ThumbnailGenerator
from TextSummarizer import TextSummarizer
from TextMostCommonWordsExtractor import TextMostCommonWordsExtractor
from FileUrlProcessor import FileUrlProcessor
import nltk


class FileProcessor:
    def __init__(self):
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

        url = file[0]
        binary = file[1]
        print("file path -> " + url)
        content, content_type = self.parser.parse_file(binary)

        thumbnail = self.thumbnail_generator.get_thumbnail(binary, content_type)

        summary = self.text_summarizer.get_summary(content)

        words, preprocessed_text = self.text_pre_processor.preprocess_text(content)

        most_common = self.text_most_common_words_extractor.get_most_common_words(words)

        timestamp, uuid, file_name = self.file_url_processor.get_timestamp_and_uuid(url)

        return url, file_name, timestamp, uuid, words, preprocessed_text, summary, most_common, thumbnail, content_type

