FROM python:3.7.3
RUN pip install coverage
RUN pip install unittest-xml-reporting
RUN apt-get update && apt-get install -y  openjdk-8-jdk
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64 
RUN wget 'https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-3.3.0.1492-linux.zip'
RUN apt-get install unzip
RUN mkdir -p /usr/local/sonar
RUN mv sonar-scanner-cli-3.3.0.1492-linux.zip /usr/local/sonar/
RUN unzip /usr/local/sonar/sonar-scanner-cli-3.3.0.1492-linux -d /usr/local/sonar/
RUN rm /usr/local/sonar/sonar-scanner-cli-3.3.0.1492-linux.zip 
ENV $PATH=$PATH:/usr/local/sonar/sonar-scanner-3.3.0.1492-linux/bin
RUN chmod -R 777 /usr/local/sonar/