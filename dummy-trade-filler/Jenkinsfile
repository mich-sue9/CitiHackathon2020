pipeline {
  agent any
 
  stages {
    stage('Test') {
      steps {
        sh './gradlew test'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }

    stage('Build Container') {
      steps {
        sh "docker build -t dummy-trade-filler:0.0.${currentBuild.number} ."
      }
    }

    stage('Deploy Container') {
      steps {
        sh "docker stop dummy-trade-filler || echo 'No dummy-trade-filler container to stop'"
        sh "docker rm dummy-trade-filler || echo 'No dummy-trade-filler container to remove'"
        sh "docker run --name dummy-trade-filler --restart unless-stopped -d --net mongo-net -e DB_HOST=mongo dummy-trade-filler:0.0.${currentBuild.number}"
      }
    }
  }
}
