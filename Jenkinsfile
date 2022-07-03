pipeline {
    agent any

//small changes
     triggers {
             // Every 5 min
             pollSCM 'H/5 * * * *'
      }

     tools {
        maven 'maven3'
     }

    stages {

      stage('Compile stage') {
                 steps {
                     bat "mvn clean compile"
                 }
       }

     
    }

  

}
