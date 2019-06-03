pipeline {
    agent any
    stages {
      stage('Docker build') {
        steps {
          sh 'docker-compose build front-end'
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
            sh 'npm install'
            sh 'npm run test:unit'
            sh 'npm run test:cy'
            sh 'npm run lintJSON || exit 0'
            sh 'npm run sonar'
          }
        }
        post {
          always {
            junit 'front-end/coverage/junit/*.xml'
            junit 'front-end/cypress/junit/*.xml'
          }
        }
      }
      stage('Push Image') {
        steps {
          script{
            docker.withRegistry("", "docker-hub-cred") {
              sh 'docker-compose push front-end'
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