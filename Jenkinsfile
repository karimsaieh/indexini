pipeline {
  agent any
  stages {
    stage('docker-compose build') {
      steps {
        echo 'I am using docker-compose to build images :D'
        sh 'docker-compose build'
      }
    }
  }
}