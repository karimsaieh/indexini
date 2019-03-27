 
FROM cloudera/quickstart 
WORKDIR /pfe
COPY . .
RUN sudo yum install python34-setuptools
RUN sudo easy_install-3.4 pip
RUN pip3 install -r requirements.txt
RUN python3 -m nltk.downloader punkt
RUN python3 -m nltk.downloader stopwords
RUN python3 -m nltk.downloader wordnet

