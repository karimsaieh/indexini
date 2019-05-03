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
      when {
        branch 'develop'
      }
      steps {
        withDockerRegistry([ credentialsId: "karimsaieh/docker-hub-cred", url: "" ]) {
          sh 'docker-compose push'	   
        }
      }
    }
  }
}
