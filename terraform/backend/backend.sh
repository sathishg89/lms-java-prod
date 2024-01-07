#!/bin/bash
sudo apt-get update -y
sudo apt-get install -y curl git

sudo curl -fsSL https://get.docker.com -o install-docker.sh
sudo sh install-docker.sh
sudo usermod -aG docker ubuntu  # Add ubuntu user to docker group

sudo git clone -b terraform-v1 https://github.com/muralialakuntla3/lms-java.git /home/ubuntu/lms-java
sudo chown -R ubuntu:ubuntu /home/ubuntu/lms-java  # Change ownership to ubuntu user

sudo docker network create -d bridge lmsnetwork
sudo docker run -d --name mysql --network lmsnetwork -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 mysql
sudo docker build -t muralialakuntla3/terraform-be /home/ubuntu/lms-java/LMS-BE
sudo docker run -d --name be --network lmsnetwork -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USERNAME=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 muralialakuntla3/terraform-be
echo "Backend Docker deployment completed" > /var/log/user-data.log 2>&1
