from nltk.cluster.util import cosine_distance
import numpy as np
import networkx as nx
import nltk
import spacy
from TextPreProcessor import TextPreProcessor


class TextSummarizer:

    def split_text_to_sentences(self, content):
        return nltk.sent_tokenize(content)

    def sentence_similarity(self, sent1, sent2):
        all_words = list(set(sent1 + sent2))

        vector1 = [0] * len(all_words)
        vector2 = [0] * len(all_words)

        # build the vector for the first sentence
        for w in sent1:
            vector1[all_words.index(w)] += 1

        # build the vector for the second sentence
        for w in sent2:
            vector2[all_words.index(w)] += 1

        return 1 - cosine_distance(vector1, vector2)

    def build_similarity_matrix(self, sentences):
        # Create an empty similarity matrix
        similarity_matrix = np.zeros((len(sentences), len(sentences)))
        for idx1 in range(len(sentences)):
            for idx2 in range(idx1):
                if idx1 == idx2: #ignore if both are same sentences
                    continue
                similarity_matrix[idx1][idx2] = self.sentence_similarity(sentences[idx1], sentences[idx2])
                similarity_matrix[idx2][idx1] = similarity_matrix[idx1][idx2]
        return similarity_matrix

    def get_summary(self, content, top_n, nlp_spacy):
        tpp = TextPreProcessor()
        # Step 1 - split it
        oroginal_sentences = self.split_text_to_sentences(content)
        pre_processed_sentences =[]
        for sentence in oroginal_sentences:
            sentence = tpp.to_lowercase(sentence)
            sentence = tpp.lemmatize(sentence, nlp_spacy)
            words = tpp.tokenize(sentence)
            words = tpp.remove_punctuation(words)
            words = tpp.remove_numbers(words)
            words = tpp.remove_one_character_words(words)
            words = tpp.remove_stopwords(words, nlp_spacy)
            pre_processed_sentences.append(words)

        # Step 2 - Generate Similary Martix across sentences
        sentence_similarity_martix = self.build_similarity_matrix(pre_processed_sentences)
        # Step 3 - Rank sentences in similarity martix
        sentence_similarity_graph = nx.from_numpy_array(sentence_similarity_martix)
        scores = nx.pagerank(sentence_similarity_graph)
        # Step 4 - Sort the rank and pick top sentences
        ranked_sentence = sorted(((scores[i], s) for i,s in enumerate(pre_processed_sentences)), reverse=True)

        summary_sentences = []
        i = 0
        while i < len(ranked_sentence) and i < top_n:
            sentence_index = pre_processed_sentences.index(ranked_sentence[i][1])
            summary_sentences.append(
                {
                    "index": sentence_index,
                    "sentence": oroginal_sentences[sentence_index]
                }
            )
            i += 1
        summary_sentences.sort(key=lambda sentence: sentence["index"], reverse=False)
        summary = ""
        for sen in summary_sentences:
            summary += "\n" + sen["sentence"]
        return summary


if __name__ == '__main__':
    # let's begin
    nlp_spacy = spacy.load("fr")
    file = open("salma.pdf", "rb").read()
    from Parser import Parser
    p = Parser()
    content, ct = p.parse_file(file)
    tm = TextSummarizer()
    summary = tm.get_summary(content, 5, nlp_spacy)
    print(summary)
