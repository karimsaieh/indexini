pipeline {
    agent any
    stages {
      stage('Docker build') {
        steps {
          echo "i'm deploying ."
        }
      }
      stage('Test') {
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
            echo "i'm deploying ."
          }
        }
      }
      stage('Push Image') {
        steps {
          script{
            docker.withRegistry("", "docker-hub-cred") {
              echo "i'm deploying ."
            }
          }
        }
      }
      stage('Deploy') {
        steps {
          script{
            echo "i'm deploying ."
          }
        }
      }
    }
  }