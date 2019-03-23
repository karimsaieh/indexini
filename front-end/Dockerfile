# build stage
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

# production stage
FROM nginx:1.13.12-alpine as production-stage
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build-stage /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
