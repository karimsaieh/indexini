import nltk
from TextPreProcessor import TextPreProcessor


class TextSummarizer:

    def get_summary(self, content, nlp_spacy):
        summary = ""
        # preprocess
        tpp = TextPreProcessor()
        # Step 1 - split it
        original_sentences = nltk.sent_tokenize(content)
        pre_processed_sentences = []
        for sentence in original_sentences:
            sentence = tpp.to_lowercase(sentence)
            sentence = tpp.lemmatize(sentence, nlp_spacy)
            words = tpp.tokenize(sentence)
            words = tpp.remove_punctuation(words)
            words = tpp.remove_numbers(words)
            words = tpp.remove_one_character_words(words)
            words = tpp.remove_stopwords(words, nlp_spacy)
            sentence = " ".join(words)
            pre_processed_sentences.append(sentence)
        pre_processed_content = " ".join(pre_processed_sentences)

        word_frequencies = {}
        for word in nltk.word_tokenize(pre_processed_content):
                if word not in word_frequencies.keys():
                    word_frequencies[word] = 1
                else:
                    word_frequencies[word] += 1
        if word_frequencies:
            maximum_frequncy = max(word_frequencies.values())
            ##
            for word in word_frequencies.keys():
                word_frequencies[word] = (word_frequencies[word]/maximum_frequncy)
            ##
            sentence_scores = []
            for index, sent in enumerate(pre_processed_sentences):
                sent_score = {
                    "index": index,
                    "score": 0,
                    "sentence": original_sentences[index]
                }
                for word in nltk.word_tokenize(sent):
                    sent_score["score"] += word_frequencies[word]
                sentence_scores.append(sent_score)

            sentence_scores = sorted(sentence_scores, key=lambda x: x["score"], reverse=True)
            sentence_scores = sentence_scores[:3]
            sentence_scores = sorted(sentence_scores, key=lambda x: x["index"], reverse=False)

            for sen in sentence_scores:
                summary += "\n" + sen["sentence"]
        return summary.strip()


if __name__ == '__main__':
    # let's begin
    import spacy
    nlp_spacy = spacy.load("fr")
    file = open("data.txt", "rb").read()
    from Parser import Parser
    p = Parser()
    content, ct = p.parse_file(file)
    tm = TextSummarizer()
    summary = tm.get_summary(content, nlp_spacy)
    print("-->", summary)
