import nltk
import unicodedata
import re
import spacy
import string

class TextPreProcessor:

    def tokenize(self, content):
        return nltk.word_tokenize(content, language="french")

    def normalize_nfc_and_remove_non_utf8(self, content):
        return unicodedata.normalize('NFC', content).encode('utf-8', 'ignore').decode('utf-8', 'ignore')

    def to_lowercase(self, content):
        return content.lower()

    def remove_punctuation(self, words):
        new_words = []
        for word in words:
            new_word = word.translate(str.maketrans('','',string.punctuation))
            if new_word != '':
                new_words.append(new_word)
        return new_words

    def remove_numbers(self, words):
        new_words = []
        for word in words:
            if not word.isdigit():
                new_words.append(word)
        return new_words

    def remove_stopwords(self, words, nlp_spacy):
        new_words = []
        for word in words:
            if word not in spacy.lang.fr.stop_words.STOP_WORDS:
                if word not in ["qu","al"]:
                    new_words.append(word)
        return new_words

    def lemmatize(self, content, nlp_spacy):
        doc = nlp_spacy(content)
        return " ".join([token.lemma_ for token in doc])

    def remove_one_character_words(self, words):
        return [word for word in words if len(word) > 1]

    def remove_newlines_and_extrasapaces(self, content):
        return re.sub("\\s+", " ", content).strip()

    # TODO: french language instead
    def normalize(self, content, nlp_spacy):
        # normalization is critical
        content = self.normalize_nfc_and_remove_non_utf8(content)
        content = self.remove_newlines_and_extrasapaces(content)
        # content for indexing
        text_content = content
        # should be before lemmatizer otherwise he'l consider some words like keywords & won't lemm them
        content = self.to_lowercase(content)
        content = self.lemmatize(content, nlp_spacy)
        words = self.tokenize(content)
        words = self.remove_punctuation(words)
        words = self.remove_numbers(words)
        words = self.remove_one_character_words(words)
        words = self.remove_stopwords(words, nlp_spacy)
        # pre processed text for machine learning
        pre_processed_text = ' '.join(x for x in words)

        return words, pre_processed_text, text_content

    def preprocess_text(self, content, nlp_spacy):
        words, pre_processed_text, content = self.normalize(content, nlp_spacy)
        return words, pre_processed_text, content


