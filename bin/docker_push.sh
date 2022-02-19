docker tag springboot-library:latest ${SB_LIB_AWS_ECR_URI}:latest
aws ecr get-login-password | docker login --password-stdin --username AWS $SB_LIB_AWS_ECR_URI
docker push ${SB_LIB_AWS_ECR_URI}:latest

