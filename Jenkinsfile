pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git(url: 'git@github.com:developer-guy/spring-data-jpa-specifications.git', branch: 'master', credentialsId: '52d539db-6c0d-4c1a-84a3-d20de355053d')
      }
    }
    stage('Clean&Verify') {
      steps {
        sh './mvnw clean verify'
      }
    }
  }
}