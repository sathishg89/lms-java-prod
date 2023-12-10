# LMS-JAVA Application VM Deployment
## Server setup:
    Server type: T2.medium server
    Database: mysql 
    Backend: java-17
    Frontend: node-20
    Ports: 22,80,8080,3306
## Setup Docker:
    sudo apt update
    curl -fsSL https://get.docker.com -o install-docker.sh
    sudo sh install-docker.sh
    sudo usermod -aG docker $USER
    newgrp docker
    
## DATABASE setup:
- docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 -e MYSQL_DATABASE=lmsdb mysql

## BACKEND setup:
- cd LMS-BE
- docker build -t lmsbe .
- docker run -d --name be --link mysql:mysql -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USERNAME=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 lmsbe

## FRONTEND Server setup:

### Connect frontend with backend  : 
    cd lms-fe/lmsv1/src/Components/
    sudo vi Home.jsx
   ** line-10:** const response = await axios.get("**http://pub-ip:8080/user/login**");

### build frontend:
    cd lms-spring/lms-fe/lmsv1
    docker build -t muralialakuntla3/lms-fe .
    docker container run -dt --name lms-fe -p 80:80 muralialakuntla3/lms-fe
    docker ps 



