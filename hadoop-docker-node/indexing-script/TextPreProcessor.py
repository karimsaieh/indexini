import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import unicodedata
import re


class TextPreProcessor:
    def __init__(self):
        # NOT IN THE EXECUTOR //so remove it ?
        self.english_stopwords = stopwords.words('english')

    def tokenize(self, content):
        return nltk.word_tokenize(content)

    def remove_non_ascii(self, words):
        new_words = []
        for word in words:
            new_word = unicodedata.normalize('NFKD', word).encode('ascii', 'ignore').decode('utf-8', 'ignore')
            new_words.append(new_word)
        return new_words

    def to_lowercase(self, words):
        new_words = []
        for word in words:
            new_word = word.lower()
            new_words.append(new_word)
        return new_words

    def remove_punctuation(self, words):
        new_words = []
        for word in words:
            new_word = re.sub(r'[^\w\s]', '', word)
            if new_word != '':
                new_words.append(new_word)
        return new_words

    def remove_numbers(self, words):
        new_words = []
        for word in words:
            if not word.isdigit():
                new_words.append(word)
        return new_words

    def remove_stopwords(self, words):
        new_words = []
        for word in words:
            if word not in self.english_stopwords:
                new_words.append(word)
        return new_words

    def lemmatize_verbs(self, words):
        lemmatizer = WordNetLemmatizer()
        lemmas = []
        for word in words:
            # TODO: lemmatizer could be enhanced by tagging words and choosing the right pos: verb, noun , adjective ..
            lemma = lemmatizer.lemmatize(word, pos='v')
            lemmas.append(lemma)
        return lemmas

    # TODO: french language instead
    def normalize(self, words):
        words = self.remove_non_ascii(words)
        words = self.to_lowercase(words)
        words = self.remove_punctuation(words)
        words = self.remove_numbers(words)
        words = self.remove_stopwords(words)
        words = self.lemmatize_verbs(words)
        pre_processed_text = ' '.join(x for x in words)
        return words, pre_processed_text

    def preprocess_text(self, content):
        words, pre_processed_text = self.normalize(self.tokenize(content))
        return words, pre_processed_text


