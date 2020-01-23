node {
    def mvnHome
    def jdk = tool name: 'JDK 11'
    env.JAVA_HOME = "${jdk}"
    mvnHome = tool 'Maven'
    stage('Preparation') { 
        checkout scm
        dir('Marmalade-App') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' clean"
            } else {
                bat(/"${mvnHome}\bin\mvn" clean/)
            }
        }
        dir('Marmalade') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' clean"
            } else {
                bat(/"${mvnHome}\bin\mvn" clean/)
            }
        }
    }
    dir('Marmalade-App') { 
		stage('Build App') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests compile"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests compile/)
            }
        }
        stage('Test App') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=true test"
            } else {
                bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore=true test/)
            }
        }
        stage('Install App') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests install"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests install/)
            }
        }
        stage('App Results') {
            junit allowEmptyResults: true, testResults: '**/TEST-*.xml'
        }
        stage('App Report') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests site"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests site/)
            }
        }
    }
    dir('Marmalade') { 
		stage('Build Marmalade') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests compile"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests compile/)
            }
        }
        stage('Test Marmalade') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore=true test"
            } else {
                bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore=true test/)
            }
        }
        stage('Install Marmalade') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests install jfx:native"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests install jfx:native/)
            }
            archiveArtifacts allowEmptyArchive: true, artifacts: 'executable/*', fingerprint: true
        }
        stage('Marmalade Results') {
            junit allowEmptyResults: true, testResults: '**/TEST-*.xml'
        }
        stage('Marmalade Report') {
            if (isUnix()) {
                sh "'${mvnHome}/bin/mvn' -DskipTests site"
            } else {
                bat(/"${mvnHome}\bin\mvn" -DskipTests site/)
            }
        }
    }
}