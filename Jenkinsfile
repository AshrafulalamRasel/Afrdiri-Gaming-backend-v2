pipeline {
    agent any

     tools {
        maven 'Maven_3.5.2'
     }

    stages {

      stage('Compile stage') {
                 steps {
                     bat "mvn clean compile"
                 }
       }

      stage('Build info') {

                steps {
                    echo "Running build: ${env.BUILD_ID} on ${env.JENKINS_URL}"
                    slackSend channel: 'jenkinsbuild', message: "Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)"
                }

       }

      stage('Code Checkout') {
                            steps {
                                checkout([$class: 'GitSCM',
                                          branches: [[name: '*/master']],
                                          doGenerateSubmoduleConfigurations: false,
                                          extensions: [],
                                          submoduleCfg: [],
                                          userRemoteConfigs: [[credentialsId: 'Git',
                                                               url: 'https://github.com/AshrafulalamRasel/Afrdiri-Gaming-backend-v2.git']]])
                            }

       }

      stage('Build') {
                   steps {
                     echo "Building.."
                      bat "mvn package"

                   }
       }

    }

}
