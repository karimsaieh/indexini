# build: docker build . -t karimsaieh/hadoop-docker-node
# run:  docker run --rm -p 8020:8020 -p 8088:8088 -p 4040:4040 -p 50070:50070 -p 8998:8998 -it karimsaieh/hadoop-docker-node
FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y  openjdk-8-jdk
RUN apt-get install -y  openssh-server
RUN apt-get install -y  wget

# install hadoop 2.6.0
RUN wget https://archive.apache.org/dist/hadoop/core/hadoop-2.6.0/hadoop-2.6.0.tar.gz
RUN tar -xzvf hadoop-2.6.0.tar.gz
RUN mv hadoop-2.6.0 /usr/local/hadoop
RUN rm hadoop-2.6.0.tar.gz

#spark 
RUN wget https://archive.apache.org/dist/spark/spark-2.4.0/spark-2.4.0-bin-hadoop2.6.tgz
RUN tar -xzvf spark-2.4.0-bin-hadoop2.6.tgz
RUN mv spark-2.4.0-bin-hadoop2.6 /usr/local/spark
RUN rm spark-2.4.0-bin-hadoop2.6.tgz


RUN wget https://www-eu.apache.org/dist/incubator/livy/0.5.0-incubating/livy-0.5.0-incubating-bin.zip
RUN apt-get -y install unzip
RUN unzip livy-0.5.0-incubating-bin.zip
RUN mv livy-0.5.0-incubating-bin /usr/local/livy
RUN rm livy-0.5.0-incubating-bin.zip

COPY config/* /tmp/


ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 
ENV HADOOP_HOME=/usr/local/hadoop
ENV SPARK_HOME=/usr/local/spark
ENV PYSPARK_PYTHON=python3
ENV PATH=$PATH:$HADOOP_HOME/bin
ENV PATH=$PATH:$HADOOP_HOME/sbin
ENV PATH=$PATH:$SPARK_HOME/bin
ENV HADOOP_MAPRED_HOME=$HADOOP_HOME
ENV HADOOP_COMMON_HOME=$HADOOP_HOME
ENV HADOOP_HDFS_HOME=$HADOOP_HOME
ENV YARN_HOME=$HADOOP_HOME
ENV HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native
ENV HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib"
ENV HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
ENV LIVY_CONF_DIR=/usr/local/livy/conf/


RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

RUN mv /tmp/ssh_config ~/.ssh/config && \
    mv /tmp/hdfs-site.xml $HADOOP_HOME/etc/hadoop/hdfs-site.xml && \
    mv /tmp/core-site.xml $HADOOP_HOME/etc/hadoop/core-site.xml && \
    mv /tmp/hadoop-env.sh $HADOOP_HOME/etc/hadoop/hadoop-env.sh && \
    mv /tmp/yarn-site.xml $HADOOP_HOME/etc/hadoop/yarn-site.xml && \
    mv /tmp/livy.conf /usr/local/livy/conf/livy.conf && \
    mv /tmp/spark-defaults.conf $SPARK_HOME/conf/spark-defaults.conf

RUN mkdir -p /usr/local/hadoop_store/hdfs/namenode
RUN mkdir -p /usr/local/hadoop_store/hdfs/datanode

RUN hadoop namenode -format

RUN chmod 600 ~/.ssh/config

RUN apt-get install -y curl net-tools iputils-ping

RUN apt-get install -y python3-pip
RUN apt-get install -y python3.6-dev
RUN pip3 install -r /tmp/requirements.txt
# RUN python3 -m nltk.downloader punkt
RUN python3 -m nltk.downloader stopwords
# RUN python3 -m nltk.downloader wordnet
RUN python3 -m spacy download fr

RUN apt-get install -y poppler-utils


COPY ./indexing-script /

#livy

RUN mv /tmp/startup.sh /startup.sh

ADD ./wait-for-it.sh /wait-for-it.sh
ENV HADOOP_OPTS="$HADOOP_OPTS -Djava.library.path=$HADOOP_HOME/lib/native"

EXPOSE 8020 50070 4040 8998 8088

CMD [ "sh", "-c", "service ssh start; /startup.sh && tail -f /dev/null"]
