# LMS-JAVA Application Docker Deployment
## Server setup:
    Server type: T2.medium server
    Ports: 22,80,8080,3306
    
## Setup Docker:
    sudo apt update
    curl -fsSL https://get.docker.com -o install-docker.sh
    sudo sh install-docker.sh
    sudo usermod -aG docker $USER
    newgrp docker
## Deployment useing docker compose file
    Install docker-compose: sudo apt install docker-compose -y
    To run the containers use : docker-compose up -d
    To stop the containers use : docker-compose down

## Database setup:
    docker network create -d bridge lmsnetwork 
    docker container run -d --name mysql --network lmsnetwork -e MYSQL_ROOT_PASSWORD=password -p 3306:3306 mysql:latest

## BACKEND BUILD:
    NOTE: update database details in **cd LMS-BE/src/main/resource/application.properties**
    **databse url:mysql://**34.242.172.238**:3306/**
    **databse pw:password**
    cd lms-spring/lms-be
    docker build -t muralialakuntla3/lms-be .
    docker container run -dt --name lms-be -p 8080:8080 muralialakuntla3/lms-be
    docker ps

##### Check backend in browser: http://pub-ip:8080/user/login

## FRONTEND BUILD:
### Connect frontend with backend  : 
    cd lms-fe/lmsv1/src/Components/
    sudo vi Home.jsx
   ** line-10:** const response = await axios.get("**http://pub-ip:8080/user/login**");

### build frontend:
    cd lms-spring/lms-fe/lmsv1
    docker build -t muralialakuntla3/lms-fe .
    docker container run -dt --name lms-fe -p 80:80 muralialakuntla3/lms-fe
    docker ps    



