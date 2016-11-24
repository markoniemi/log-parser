docker.image('maven:alpine').inside {
  sh 'env > env.txt'
  //def mvnHome = tool 'Maven 3.3'
  stage ('Checkout') {
    git credentialsId: 'cfdbaaff-7fae-40f8-a2fd-2da5731b9ed2', url: 'http://gogs:3000/niemimac/log-parser.git'
  }
  stage ('Build') {
    //env.JAVA_HOME="${tool 'JDK 1.8'}"
    //env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
    sh 'java -version'   
    sh "mvn -Dmaven.test.failure.ignore clean package -DskipTests=true"
  }
  stage ('Test') {
    sh "mvn -Dmaven.test.failure.ignore package"
    step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
  }
  stage ('Integration test') {
    sh "mvn -Dmaven.test.failure.ignore install"
    step([$class: 'JUnitResultArchiver', testResults: '**/target/failsafe-reports/TEST-*.xml'])
  }
  stage ('Site') {
    sh "mvn -Dmaven.test.failure.ignore -DskipTests=true site"
    publishHTML(target: [
        reportName : 'Maven Site',
        reportDir:   'target/site',
        reportFiles: 'index.html',
        keepAll:     true,
        alwaysLinkToLastBuild: true,
        allowMissing: true
    ])    
  }
  stage ('Sonar') {
    if (isTimeBetween(13,19)){
      sh "mvn -Dmaven.test.failure.ignore sonar:sonar -DskipTests=true -Dsonar.host.url=${env.SONAR_URL}"
    } else {
        println "Skipping Sonar"
    }
  }
}

boolean isTimeBetween(startHour, endHour){
    def currentTime = new java.util.Date();
    return currentTime.getHours() >= 13 && currentTime.getHours() <= 19
}