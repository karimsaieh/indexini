import spacy
nlp = spacy.load('fr')
print("j")




doc = nlp(u"cadrés cadrées  cadre   ")
# doc = nlp(u"sms")
print(spacy.lang.fr.stop_words.STOP_WORDS)

for token in doc:
    print(token, token.lemma_)



