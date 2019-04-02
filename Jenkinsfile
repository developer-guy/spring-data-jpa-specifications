pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'git@github.com:developer-guy/spring-data-jpa-specifications.git', branch: 'master', credentialsId: 'jenkins-github', poll: true)
      }
    }
    stage('verify') {
      steps {
        sh './mvnw clean verify'
      }
    }
  }
}