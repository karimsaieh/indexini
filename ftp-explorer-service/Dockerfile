FROM python:3.6.7-alpine

WORKDIR /usr/src/app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

ENV RABBITMQ_HOST=rabbitmq

ADD ./wait-for-it.sh /wait-for-it.sh
RUN apk add --no-cache bash

CMD [ "python", "./Main.py" ]