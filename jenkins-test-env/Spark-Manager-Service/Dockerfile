FROM ubuntu 

RUN apt-get update
RUN apt-get install -y  openjdk-8-jdk
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 
RUN apt-get update && apt-get install -y maven

RUN apt-get install -y gnupg2

RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv E52529D4
RUN bash -c 'echo "deb http://repo.mongodb.org/apt/ubuntu xenial/mongodb-org/4.0 multiverse" > /etc/apt/sources.list.d/mongodb-org-4.0.list'
RUN apt update
RUN apt install -y mongodb-org
RUN apt-get install -y systemd
RUN mkdir -p /data/db
RUN (mongod &) && sleep 20
RUN mongod --version
