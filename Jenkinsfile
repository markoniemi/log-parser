node {
   sh 'env > env.txt'
stage 'Checkout'
   git credentialsId: 'ca28746a-6fc9-4cf4-a6ed-a8b00eb8fa55', url: 'https://niemimac@bitbucket.org/niemimac/log-parser.git'
   def mvnHome = tool 'Maven 3.3'
stage 'Build'
   env.JAVA_HOME="${tool 'JDK 1.8'}"
   //env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
   sh 'java -version'   
   sh "${mvnHome}/bin/mvn -Dmaven.test.failure.ignore clean package -DskipTests=true"
stage 'Test'
   sh "${mvnHome}/bin/mvn -Dmaven.test.failure.ignore package"
   step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
stage 'Sonar'
   sh "${mvnHome}/bin/mvn -Dmaven.test.failure.ignore sonar:sonar -DskipTests=true -Dsonar.host.url=${env.SONAR_URL}"
}
