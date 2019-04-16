import spacy
nlp = spacy.load('fr')

doc = nlp(u"voudrais non animaux yeux dors couvre.")

for token in doc:
    print(token, token.lemma_)

