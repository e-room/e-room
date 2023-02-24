pipeline {
    agent any

    environment {
        stage = getStageName(env.BRANCH_NAME)
        imagename = "larrykwon/eroom-api-" + stage
        registryCredential = 'docker-hub'
        dockerImage = ''
    }

    stages {
        stage('Prepare') {
            steps {
                echo 'Clonning Repository'
                sh 'pwd'
                echo 'Pulling...' + stage
                git url: 'https://github.com/e-room/e-room.git',
                        branch: env.BRANCH_NAME,
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
                script {
                    dockerImage = docker.build imagename
                }
            }
            post {
                failure {
                    error 'This pipeline stops here...'
                }
            }
        }

        stage('Push Docker') {
            steps {
                echo 'Push Docker' + 'stage: ' + env.BRANCH_NAME
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
                if (stage == 'production') {
                    sshagent(credentials: ['ssh']) {
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_PROD} 'docker pull larrykwon/eroom-api-production:latest'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_PROD} 'docker ps -aq --filter name=eroom-api-production | grep . && docker rm -f \$(docker ps -aq --filter name=eroom-api-production)'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_PROD} 'docker run -d --name eroom-api-production -p 8080:8080 larrykwon/eroom-api-production:latest'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_PROD} 'yes y | docker image prune'"
                    }
                } else {
                    sshagent(credentials: ['ssh']) {
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_DEV} 'docker pull larrykwon/eroom-api-develop:latest'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_DEV} 'docker ps -aq --filter name=eroom-api-develop | grep . && docker rm -f \$(docker ps -aq --filter name=eroom-api-develop)'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_DEV} 'docker run -d --name eroom-api-develop -p 8080:8080 larrykwon/eroom-api-develop:latest'"
                        sh "ssh -o StrictHostKeyChecking=no ${env.EROOM_API_DEV} 'yes y | docker image prune'"
                    }
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


def getStageName(branchName) {
    if ("master".equals(branchName)) {
        return "production";
    }
    return "develop";
}
