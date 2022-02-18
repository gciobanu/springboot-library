AWS_ECR_URI="222574671412.dkr.ecr.eu-west-1.amazonaws.com/springboot-library"

docker tag springboot-library:latest ${AWS_ECR_URI}:latest
aws ecr get-login-password | docker login --password-stdin --username AWS $AWS_ECR_URI
docker push ${AWS_ECR_URI}:latest

