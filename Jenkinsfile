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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t $IMAGE_NAME .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh '''
                    docker rm -f $CONTAINER_NAME || true
                    docker run -d -p $APP_PORT:$APP_PORT --name $CONTAINER_NAME $IMAGE_NAME
                '''
            }
        }
    }
}
