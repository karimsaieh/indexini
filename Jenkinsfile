pipeline {
    agent any
    stages {
      stage('Test') {
        parallel {
          stage('Test-Front-End') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-vue-test-env'
              }
            }
            steps {
              dir(path: 'front-end') {
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
            steps {
              dir(path: 'notification-service') {
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