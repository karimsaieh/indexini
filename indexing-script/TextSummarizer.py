from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from nltk.tokenize import word_tokenize, sent_tokenize
import nltk
import re

class TextSummarizer:

    def __init__(self):
        self.english_stopwords = stopwords.words('english')

    def create_frequency_table(self, text_string) -> dict:
        #TODO: change language to french ?
        stop_words = set(self.english_stopwords)
        words = word_tokenize(text_string)
        ps = PorterStemmer()
        freq_table = dict()
        for word in words:
            # TODO: lemmatization & a french one
            word = ps.stem(word)
            if word not in stop_words:
                if word in freq_table:
                    freq_table[word] += 1
                else:
                    freq_table[word] = 1
        return freq_table

    def score_sentences(self, sentences, freq_table) -> dict:
        sentence_value = dict()
        for sentence in sentences:
            word_count_in_sentence = (len(word_tokenize(sentence)))
            for wordValue in freq_table:
                if wordValue in sentence.lower():
                    if sentence[:100] in sentence_value:
                        sentence_value[sentence[:100]] += freq_table[wordValue]
                    else:
                        sentence_value[sentence[:100]] = freq_table[wordValue]
            sentence_value[sentence[:100]] = sentence_value[sentence[:100]] // word_count_in_sentence
        return sentence_value

    def find_average_score(self, sentence_value) -> int:
        sum_values = 0
        for entry in sentence_value:
            sum_values += sentence_value[entry]
        average = int(sum_values / len(sentence_value))
        return average

    def generate_summary(self, sentences, sentence_value, threshold):
        sentence_count = 0
        summary = ''
        for sentence in sentences:
            if sentence[:100] in sentence_value and sentence_value[sentence[:100]] > threshold:
                summary += " " + sentence
                sentence_count += 1
        summary = re.sub(r'\n\s*\n', '\n', summary)
        return summary

    def get_summary(self, content):
        summary = ""
        if content:
            freq_table = self.create_frequency_table(content.lower())
            sentences = sent_tokenize(content)
            sentence_scores = self.score_sentences(sentences, freq_table)
            threshold = self.find_average_score(sentence_scores)
            summary = self.generate_summary(sentences, sentence_scores, 1.2 * threshold)
        return summary
