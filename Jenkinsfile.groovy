pipeline {
    agent any

    environment {
        imagename = "larrykwon/eroom-api"
        registryCredential = 'docker-hub'
        dockerImage = ''
    }

    stages {
        stage('Prepare') {
            steps {
                echo 'Clonning Repository'
                sh 'pwd'
                sh 'ls -al'
                git url: 'https://github.com/e-room/e-room.git',
                        branch: 'develop',
                        credentialsId: 'github'
                sh 'ls -al'
            }
            post {
                success {
                    echo 'Successfully Cloned Repository'
                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Bulid Gradle') {
            steps {
                echo 'Bulid Gradle'
                sh 'pwd'
                withGradle {
                    sh './gradlew clean build --exclude-task test'
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Bulid Docker') {
            steps {
                echo 'Bulid Docker'
                sh 'pwd'
                // sh 'cd ./eroom-api-docker/eroom-api-dockerfile'
                // dir('eroom-api-docker/eroom-api-dockerfile'){
                script {
                    dockerImage = docker.build imagename
                }
                // }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Push Docker') {
            steps {
                echo 'Push Docker'
                sh 'ls -al'
                script {
                    docker.withRegistry('', registryCredential) {
                        dockerImage.push()
                    }
                }
            }
            post {
                success {
                    sh 'yes y | docker image prune'

                }
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Docker Run') {
            steps {
                echo 'Pull Docker Image & Docker Image Run'
                sshagent(credentials: ['ssh']) {
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@ec2-43-200-50-204.ap-northeast-2.compute.amazonaws.com 'docker pull larrykwon/eroom-api:latest'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@ec2-43-200-50-204.ap-northeast-2.compute.amazonaws.com 'docker ps -aq --filter name=eroom-api-core | grep . && docker rm -f \$(docker ps -aq --filter name=eroom-api-core)'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@ec2-43-200-50-204.ap-northeast-2.compute.amazonaws.com 'docker run -d --name eroom-api-core -p 8080:8080 larrykwon/eroom-api:latest'"
                    sh "ssh -o StrictHostKeyChecking=no ubuntu@ec2-43-200-50-204.ap-northeast-2.compute.amazonaws.com 'yes y | docker image prune'"


                }
            }
        }
    }
    post {
        success {
            slackSend(channel: '#tech-deploy', color: '#00FF00', message: "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
        failure {
            slackSend(channel: '#tech-deploy', color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }
    }
}
