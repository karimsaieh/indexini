import nltk


class TextMostCommonWordsExtractor:

    def get_most_common_words(self, file):
        number = 30
        words = file[1]
        word_freq_dist = nltk.FreqDist(words)
        most_common = dict(word_freq_dist.most_common(number))
        return file[0], file[1], file[2], file[3], most_common, file[4], file[5]
