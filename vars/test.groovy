def call( Map param ) {
    def hostname = param.hostname
    pipeline {
        agent {
            node {
                label 'jenkins'
            }
        }
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
            }
            stage('Deliver') {
                steps {
                    sh 'ssh root@${param.hostname} "rm -rf /opt/dist && mkdir -p /opt/dist"'
                    sh 'scp target/${NAME}-${VERSION}.jar root@${param.hostname}:/opt/dist'
                    sh 'ssh root@${param.hostname} "java -jar /opt/dist/${NAME}-${VERSION}.jar"'
                }
            }
        }
    }
}
