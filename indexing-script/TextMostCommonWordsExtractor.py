import nltk


class TextMostCommonWordsExtractor:

    def get_most_common_words(self, words):
        number = 30
        word_freq_dist = nltk.FreqDist(words)
        most_common = dict(word_freq_dist.most_common(number))
        return most_common
