# docker build . -t karimsaieh/notification-service

FROM node:11.13.0-alpine

WORKDIR /usr/src/app
COPY package*.json ./
RUN npm install
COPY . .

ADD ./wait-for-it.sh /wait-for-it.sh
RUN apk add --no-cache bash

EXPOSE 3010
CMD [ "npm", "run", "start-prod" ]