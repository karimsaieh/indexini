pipeline {
    agent any
    stages {
      stage('docker-compose build') {
        steps {
          echo 'I am using docker-compose to build images :D'
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
              }
            }
            environment {
              HOME = '/tmp'
              npm_config_cache = 'npm-cache'
            }
            steps {
              dir(path: 'front-end') {
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
              }
            }
          }
        }
      }
      stage('Push Images') {
        steps {
          script{
            docker.withRegistry("", "docker-hub-cred") {
            }
          }
        }
      }
      stage('Deployment') {
        when {
          branch 'develop'
        }
        steps {
          echo 'I am using docker-compose to build images :D'
        }
      }
    }
  }