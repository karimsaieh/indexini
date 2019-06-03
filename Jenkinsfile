pipeline {
    agent any
    stages {
      stage('docker build') {
        steps {
          echo 'I am using docker-compose to build images :D'
          // sh 'docker-compose build front-end'
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
            // sh 'npm install'
            // sh 'npm run test:unit'
            // sh 'npm run test:cy'
            // sh 'npm run lintJSON || exit 0'
            // sh 'npm run sonar'
            echo "i ama testing"
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
            // sh 'docker-compose push front-end'
            echo "i aa m pushing"
            }
          }
        }
      }
      stage('Deployment') {
        when {
          branch 'develop'
        }
        steps {
          script{
            // git update-index --chmod=+x staging-deploy.sh
            // sh './deploy-prod.sh || exit 0'
            // sh 'sed -ie "s/FORCE_REDEPLOY_VALUE/$(date)/g" kubernetes/*.yaml'
            // sh 'cat ./kubernetes/web-scraping-service-deployment.yaml'
            // sh 'ansible-playbook playbook.yml'
            echo "i'm deploying"
          }
        }
      }
    }
  }