# this file is designed to run on the server
AWS_ECR_URI='MY_ECR_URI'
APP_NAME='springboot-library'

docker rm -f $APP_NAME
docker rmi -f $(docker images -a -q)
aws ecr get-login-password --region eu-west-1 | docker login --password-stdin --username AWS "${AWS_ECR_URI}"
docker run --name $APP_NAME --restart unless-stopped -d -p 80:8080 "${AWS_ECR_URI}:latest"

