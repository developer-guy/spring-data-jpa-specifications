pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git(url: 'git@github.com:developer-guy/spring-data-jpa-specifications.git', branch: 'master', credentialsId: 'jenkins-github')
      }
    }
    stage('Clean&Verify') {
      steps {
        sh './mvnw clean verify'
      }
    }
  }
}
