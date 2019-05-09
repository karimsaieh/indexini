# docker build . -t karimsaieh/web-scraping-service
FROM python:3.6.7-alpine

WORKDIR /usr/src/app

COPY requirements.txt ./

RUN apk add build-base libxml2-dev libxslt-dev

RUN pip install --no-cache-dir -r requirements.txt

RUN apk del build-base

RUN apk add --no-cache bash

COPY . .

ADD ./wait-for-it.sh /wait-for-it.sh
RUN chmod 777 /wait-for-it.sh

CMD [ "python", "./Main.py" ]