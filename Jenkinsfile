pipeline {
  agent any
  stages {
    stage('docker-compose build') {
      steps {
        echo 'I am using docker-compose to build images :D'
        sh 'docker-compose build'
      }
    }
    stage('Tests') {
      steps {
        echo 'pretend to be testing'
      }
    }
    stage('Push Images') {
        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-cred') {
              sh 'docker-compose push'
            } 
        echo "Trying to Push Docker Build to DockerHub"
    }
  }
}
