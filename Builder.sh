mvn clean install
docker build -t dockerhub/tag -f ./DockerFile .
sudo docker push dockerhub/tag:latestt