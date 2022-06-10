def isStringFoundInLastCommit(String s){
    return sh (
            script: "git log -1 --pretty=%B | grep ${s}",
            returnStatus: true
    ) == 0
}
def incrementPatchVersion(){
    echo "incrementing project patch version"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} versions:commit'
    version = readMavenPom().getVersion()
    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
    echo "the new version is : ${version}"
}
def incrementMinorVersion(){
    echo "incrementing project minor version"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.nextMinorVersion}.\\\${parsedVersion.incrementalVersion} versions:commit'
    version = readMavenPom().getVersion()
    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
    echo "the new version is : ${version}"
}

def incrementMajorVersion(){
    echo "incrementing project major version"
    sh 'mvn build-helper:parse-version versions:set -DnewVersion=\\\${parsedVersion.nextMajorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.incrementalVersion} versions:commit'
    version = readMavenPom().getVersion()
    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
    echo "the new version is : ${version}"
}
def test(){
    sh 'mvn test'
}
def buildImage(){
    echo "${IMAGE_TAG}"
    sh "docker build -t oubaydos/temp:${IMAGE_TAG} ."
}
def pushImage(){
    echo "pushing the docker image"
    // TODO pushed to nexus
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]){
        sh "echo $PASSWORD | docker login -u $USERNAME --password-stdin"
        sh "docker push oubaydos/temp:$IMAGE_TAG"
    }
}
def deployImage() {
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
        sshagent(credentials: ['ec2-deployment-instance']) {
            // TODO remove old container
            sh "ssh -o StrictHostKeyChecking=no ubuntu@3.86.242.247 docker login -u $USERNAME -p $PASSWORD"
            sh "ssh -o StrictHostKeyChecking=no ubuntu@3.86.242.247 docker rm -f myimage || true"
            sh "ssh -o StrictHostKeyChecking=no ubuntu@3.86.242.247 docker run --name myimage -p 7070:8080 -d oubaydos/temp:$IMAGE_TAG"
        }
    }
}

return this