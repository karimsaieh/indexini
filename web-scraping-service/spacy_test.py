import spacy
nlp = spacy.load('fr')

doc = nlp(u"voudrais non animaux yeux dors couvre.  travail travaille a qui que pour le Boots and hippos aren't.")

for token in doc:
    print(token, token.lemma,token.pos_,token.dep_, token.lemma_)

# print(spacy.lang.fr.stop_words.STOP_WORDS)
