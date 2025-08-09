pipeline {
    agent any

    environment {
        IMAGE_NAME = "medical-register"
        CONTAINER_NAME = "medical-register"
        APP_PORT = "8081"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/RaviKotapati/Medical-Reg.git'
            }
        }

        stage('Build JAR (Gradle)') {
            steps {
                bat 'gradlew.bat clean build'
            }
        }

        stage('Debug: Show Workspace & JAR') {
            steps {
                bat '''
                echo ===== Current Workspace =====
                echo %CD%
                echo ===== Contents of build/libs =====
                dir build\\libs
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %IMAGE_NAME% ."
            }
        }

        stage('Run Docker Container') {
            steps {
                bat """
                docker rm -f %CONTAINER_NAME% || exit 0
                docker run -d -p %APP_PORT%:%APP_PORT% --name %CONTAINER_NAME% %IMAGE_NAME%
                """
            }
        }
    }
}
