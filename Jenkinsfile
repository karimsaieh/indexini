pipeline {
    agent any
    stages {
      stage('Test') {
        parallel {
          stage('Test-Web-Scraping-Service') {
            agent {
              docker {
                image 'python:3.7.3'
              }
            }
            steps {
              dir(path: 'web-scraping-service') {
                sh 'python -m unittest'
              }
            }
          }
          stage('Test-Ftp-Explorer-Service') {
            agent {
              docker {
                image 'python:3.7.3'
              }
            }
            steps {
              dir(path: 'ftp-explorer-service') {
                sh 'python -m unittest tests.test_utils'
              }
            }
          }
          stage('Test-Front-End') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-vue-test-env'
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
          stage('Test-Spark-Manager-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
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
                sh 'ls'
                sh 'mvn test -Dspring.profiles.active=dev'
              }
            }
          }
          stage('pardfghal3___') {
            steps {
              echo 'dfgs'
            }
          }
          stage('pargffghddhal3') {
            steps {
              echo 'dfgs'
            }
          }
          stage('pdharafgdhl3') {
            steps {
              echo 'dfgs'
            }
          }
          stage('parfdhdal3') {
            steps {
              echo 'dfgs'
            }
          }
        }
      }
    }
  }