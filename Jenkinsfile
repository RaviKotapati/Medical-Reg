pipeline {
    agent any

    environment {
        IMAGE_NAME = "medical-register"
        CONTAINER_NAME = "medical-register"
        APP_PORT = "8001"
    }

    stages {
        stage('Clone Code') {
            steps {
                git 'https://github.com/RaviKotapati/Medical-Reg.git'
            }
        }

        stage('Build JAR') {
            steps {
                bat 'mvn clean package -DskipTests'
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