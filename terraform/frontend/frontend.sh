#!/bin/bash

sudo apt-get update -y
sudo apt-get install -y curl git

sudo curl -fsSL https://get.docker.com -o install-docker.sh
sudo sh install-docker.sh
sudo usermod -aG docker ubuntu

sudo git clone -b terraform https://github.com/muralialakuntla3/lms-java.git /home/ubuntu/lms-java
sudo chown -R ubuntu:ubuntu /home/ubuntu/lms-java

sudo docker network create -d bridge lmsnetwork
sudo docker build -t muralialakuntla3/terraform-fe /home/ubuntu/lms-java/lms-fe/lmsv1
sudo docker run -d --name fe --network lmsnetwork -p 80:80 muralialakuntla3/terraform-fe

echo "Frontend Docker deployment completed" > /var/log/user-data.log 2>&1
