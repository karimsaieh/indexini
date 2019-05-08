pipeline {
    agent any
    stages {
      stage('docker-compose build') {
        steps {
          echo 'I am using docker-compose to build images :D'
          sh 'docker-compose build'
        }
      }
      stage('Test') {
        parallel {
          stage('Test-Notification-Service') {
            agent {
              docker {
                image 'node:11.13.0'
                args '--network="host"' 
              }
            }
            environment {
              HOME = '.'
              npm_config_cache = 'npm-cache'
            }
            steps {
              dir(path: 'notification-service') {
                sh 'npm install'
                sh 'npm run test'
                sh 'npm run lintJSON'
                sh 'npm run sonar'
              }
            }
            post {
              always {
                junit 'notification-service/coverage/junit/junit.xml'
              }
            }
          }
          stage('Test-Front-End') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-vue-test-env:cy'
                args '--network="host"'
                reuseNode true
              }
            }
            environment {
              HOME = '/tmp'
              npm_config_cache = 'npm-cache'
            }
            steps {
              dir(path: 'front-end') {
                sh 'npm install'
                sh 'npm run test:unit'
                sh 'npm run test:cy'
                sh 'npm run lintJSON'
                sh 'npm run sonar'
              }
            }
            post {
              always {
                junit 'front-end/coverage/junit/junit.xml'
                junit 'front-end/cypress/junit/*.xml'
              }
            }
          }
          stage('Test-Web-Scraping-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-python-test-env'
                args '--network="host"'
              }
            }
            environment {
              HOME = '/tmp'
            }
            steps {
              dir(path: 'web-scraping-service') {
                sh 'coverage run -m xmlrunner discover -o junit'
                sh 'coverage xml'
                sh '/usr/local/sonar/sonar-scanner-3.3.0.1492-linux/bin/sonar-scanner'
              }
            }
            post {
              always {
                junit 'web-scraping-service/junit/*.xml'
              }
            }
          }
          stage('Test-Ftp-Explorer-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-python-test-env'
                args '--network="host"'
              }
            }
            steps {
              dir(path: 'ftp-explorer-service') {
                sh 'python -m unittest tests.test_utils'
                sh '/usr/local/sonar/sonar-scanner-3.3.0.1492-linux/bin/sonar-scanner'
              }
            }
          }
          stage('Test-Spark-Manager-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-spark-manager-service-test-env'
                args '-v /root/.m2:/root/.m2 --network="host"' 
              }
            }
            post {
              always {
                junit 'spark-manager-service/target/surefire-reports/*.xml'
              }
            }
            steps {
              dir(path: 'spark-manager-service') {
                sh '(mongod &) && sleep 30 && echo "done waiting for mongo"'
                sh '(mvn spring-boot:run -Dspring.profiles.active=dev &) && (while ! echo "waiting" | nc localhost 3013; do sleep 1 && echo waiting; done) && (echo "gatling will start soon" && sleep 10) && (mvn gatling:test)'
                gatlingArchive()
                sh 'mvn clean package test -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
          stage('Test-File-Management-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2 --network="host"'
              }
            }
            post {
              always {
                junit 'file-management-service/target/surefire-reports/*.xml'
  
              }
            }
            steps {
              dir(path: 'file-management-service') {
                sh 'ls'
                sh 'mvn clean package test -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
          stage('Test-Search-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2 --network="host"'
              }
            }
            post {
              always {
                junit 'search-service/target/surefire-reports/*.xml'
  
              }
            }
            steps {
              dir(path: 'search-service') {
                sh 'ls'
                sh 'mvn clean package test -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
        }
      }
      stage('Push Images') {
        steps {
          script{
            docker.withRegistry("", "docker-hub-cred") {
            sh 'docker-compose push'
            }
          }
        }
      }
    }
  }