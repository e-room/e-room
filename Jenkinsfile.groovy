def getSplitBranchName(branchName) {
    def branchArray = env.GIT_BRANCH.split("/")
    script {
        echo "branchName ${branchArray[1]}"
    }
    return branchArray[1]
}

def getStageName(branchName) {
    if ("master".equals(branchName)) {
        return "production";
    }
    return "develop";
}

def branchName
def deployStage
def imageName

pipeline {
    agent any

    environment {
        registryCredential = 'docker-hub'
        dockerImage = ''
    }

    stages {
        stage('initialize variables') {
            steps {
                checkout scm
                script {
                    branchName = env.GIT_BRANCH.split("/")[1]
                    deployStage = getStageName("${branchName}")
                    imageName = "larrykwon/eroom-api-" + "${deployStage}"
                }
            }
            post {
                success {
                    echo "${branchName}"
                    echo "${deployStage}"
                    echo "${imageName}"
                }
                failure {
                    error "fail while initializing"
                }
            }
        }


        stage('Prepare') {
            steps {
                echo 'Clonning Repository'
                sh 'pwd'
                echo 'Pulling...' + deployStage
                git url: 'https://github.com/e-room/e-room.git',
                        branch: branchName,
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
                    dockerImage = docker.build imageName
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
                echo 'Push Docker' + 'stage: ' + "${deployStage}"
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
                script {
                    if (deployStage == 'production') {
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
