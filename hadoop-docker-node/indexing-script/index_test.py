import nltk
import unicodedata
import re
import spacy
from Parser import  Parser
from FileProcessor import  FileProcessor
from TextPreProcessor import TextPreProcessor

import json
import spacy

fp = FileProcessor()
p = Parser()
tpp = TextPreProcessor()
file = open("rapport.pdf", "rb").read()

fp.process(("/pfe/sd/sd:sd/sd:sd/", file))

nlp_spacy = spacy.load("fr")
content, content_type = p.parse_file(file)
# words, content_type, content = tpp.preprocess_text("t- - pp les obstacles9 5 0ç  à école éllève l'élève qu'affaire FAVEUR. \n\n\t Qu’il a rencontrés", nlp_spacy)
words, content_type, content = tpp.preprocess_text(content, nlp_spacy)
data = {}
data["content"] = content
data["content_type"] = content_type
data["words"] = words

with open('data.json', 'w') as outfile:
    json.dump(data, outfile)
