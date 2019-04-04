# docker build . -t karimsaieh/front-end
# build stage docker run --rm -p 3111:80 karimsaieh/front-end
FROM node:9.11.1-alpine as build-stage
RUN apk update && apk upgrade && \
  apk add --no-cache bash git openssh
RUN apk add --update \
  python \
  python-dev \
  py-pip \
  build-base \
  && pip install virtualenv \
  && rm -rf /var/cache/apk/*
WORKDIR /app
COPY package*.json ./
RUN npm install --loglevel verbose
COPY . .
RUN npm run build:prod

# docker build . -t karimsaieh/notification-service
FROM node:11.13.0-alpine as production-stage
WORKDIR /usr/src/app
COPY ./server/package*.json ./
RUN npm install
COPY ./server .
COPY --from=build-stage /app/dist /usr/src/app/dist
ADD ./wait-for-it.sh /wait-for-it.sh
RUN apk add --no-cache bash
EXPOSE 3000
CMD [ "npm", "start" ]


