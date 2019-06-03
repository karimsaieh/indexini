pipeline {
    agent any
    stages {
      stage('docker-compose build') {
         parallel {
          stage('Build-Notification-Service') {
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
                echo 'hello'
              }
            }
          }
          stage('Build-Front-End') {
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
                echo 'hello'
              }
            }
          }
          stage('Build-Web-Scraping-Service') {
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
                echo 'hello'
              }
            }
          }
          stage('BUild-Ftp-Explorer-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-python-test-env'
                args '--network="host"'
              }
            }
            steps {
              dir(path: 'ftp-explorer-service') {
                echo 'hello'
              }
            }
          }
          stage('Build-Spark-Manager-Service') {
            agent {
              docker {
                image 'karimsaieh/jenkins-pfe-spark-manager-service-test-env'
                args '-v /root/.m2:/root/.m2 --network="host"' 
              }
            }
            steps {
              dir(path: 'spark-manager-service') {
                echo 'hello'
              }
            }
          }
          stage('Build-File-Management-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2 --network="host"'
              }
            }
            steps {
              dir(path: 'file-management-service') {
                echo 'hello'
              }
            }
          }
          stage('Build-Search-Service') {
            agent {
              docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2 --network="host"'
              }
            }
            steps {
              dir(path: 'search-service') {
                echo 'hello'
              }
            }
          }
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
                echo 'hello'
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
                echo 'hello'
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
                echo 'hello'
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
                echo 'hello'
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
            steps {
              dir(path: 'spark-manager-service') {
                echo 'hello'
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
            steps {
              dir(path: 'file-management-service') {
                echo 'hello'
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
            steps {
              dir(path: 'search-service') {
                echo 'hello'
              }
            }
          }
        }
      }
      stage('Push Images') {
        steps {
          script{
            docker.withRegistry("", "docker-hub-cred") {
              echo 'hello'
            }
          }
        }
      }

      stage('Deploy to staging') {
        when {
          branch 'develop'
        }
        steps {
          echo 'I am using docker-compose to build images :D'
        }
      }
      stage('Deploy to production') {
        when {
          branch 'jenkins'
        }
        steps {
          echo 'I am using docker-compose to build images :D'
        }
      }
    }
  }