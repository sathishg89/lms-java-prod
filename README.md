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
    
## DOCKER NETWORK setup:
- docker network create -d bridge lmsnetwork
    
## DATABASE setup:
- docker run -dt --name mysql --network lmsnetwork -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 mysql

## BACKEND setup:
- cd ~/lms-java/LMS-BE
- docker build -t lmsbe .
- docker run -dt --name be --network lmsnetwork -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USER=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 lmsbe
### Check backend in browser
- browse : pub-ip:8080/user/login

## FRONTEND Server setup:

### Connect frontend with backend  : 
- cd ~/lms-java/LMS-FE/src/
- sudo vi utils.js
- export const url =("**http://pub-ip:8080/user/**");

### build frontend:
- cd ~/lms-java/LMS-FE
- docker build -t lmsfe .
- docker run -dt --name fe --network lmsnetwork -p 80:80 lmsfe
- docker ps
### Check frontend in browser
- browse : pub-ip:80
- click on **Test**
- it will show **Connected To Back-End**
