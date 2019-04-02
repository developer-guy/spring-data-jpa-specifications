pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'git@github.com:developer-guy/spring-data-jpa-specifications.git', branch: 'master', poll: true, credentialsId: 'jenkins')
      }
    }
    stage('verify') {
      steps {
        sh './mvnw clean verify'
      }
    }
  }
}