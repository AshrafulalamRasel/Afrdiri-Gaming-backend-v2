pipeline {
    agent any



    stages {


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
                                          userRemoteConfigs: [[credentialsId: 'jenkins-user-github',
                                                               url: 'https://github.com/AshrafulalamRasel/Afrdiri-Gaming-backend-v2.git']]])
                            }

       }

      stage('Build') {
     	                  steps {
     	                      echo 'Building..'
     	                      sh "mvn package"
     	                  }
       }

    }

}
