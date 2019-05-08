pipeline {
    agent any
    stages {
      stage('SonarQube QA') {
        parallel {
          stage('Spark-Manager-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /home/karim/.m2:/root/.m2 --network="host"'
              }
            }
            steps {
              dir(path: 'spark-manager-service') {
                sh 'mvn clean package -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
          stage('File-Management-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /home/karim/.m2:/root/.m2 --network="host"'
              }
            }
            steps {
              dir(path: 'file-management-service') {
                sh 'mvn clean package -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
          stage('Search-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /home/karim/.m2:/root/.m2 --network="host"'
              }
            }
            steps {
              dir(path: 'search-service') {
                sh 'mvn clean package -Dspring.profiles.active=dev sonar:sonar'
              }
            }
          }
        }
      }
      stage('docker-compose build') {
        steps {
          echo 'I am using docker-compose to build images :D'
          sh 'docker-compose build'
        }
      }
      stage('Test') {
        parallel {
          stage('Test-Spark-Manager-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-spark-manager-service-test-env'
                args '-v /root/.m2:/root/.m2' 
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
                sh 'mvn test -Dspring.profiles.active=dev'
              }
            }
          }
          stage('Test-Front-End') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-vue-test-env:cy'
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
              }
            }
            environment {
              HOME = '/tmp'
            }
            steps {
              dir(path: 'web-scraping-service') {
                sh 'coverage run -m xmlrunner discover -o junit'
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
              }
            }
            steps {
              dir(path: 'ftp-explorer-service') {
                sh 'python -m unittest tests.test_utils'
              }
            }
          }
          stage('Test-Notification-Service') {
            agent {
              docker {
                image 'node:11.13.0-alpine'
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
              }
            }
            post {
              always {
                junit 'notification-service/coverage/junit/junit.xml'
              }
            }
          }
          stage('Test-File-Management-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2'
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
                sh 'mvn test -Dspring.profiles.active=dev'
              }
            }
          }
          stage('Test-Search-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2'
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
                sh 'mvn test -Dspring.profiles.active=dev'
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