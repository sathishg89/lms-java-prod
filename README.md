# LMS-JAVA Application Manual Deployment
## Server setup:
    Server type: T2.medium server
    Database: mysql 
    Backend: java-17
    Frontend: node-20
    Ports: 22,80,8080,3306

#### Install tree for checking directories: sudo apt  install tree -y
## Database setup:
    Install mysql db: sudo apt install mysql-server -y
    Mysql secure installations: sudo mysql_secure_installation
        Choose option: y/n
    Check status: sudo service mysql status
    Restart service: sudo service mysql restart
### Password setup:
    sudo mysql -u root -p
    Enter password: empty+enter
    ### Password setup query:
        ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
        FLUSH PRIVILEGES;
        EXIT;

## BACKEND setup:
### Update DB credentials: LMS-BE/src/main/resources/application.properties
- Change/Update the DataBase details according to your requirement 
    spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:${DB_PORT:3306}/${DB_NAME:LMS}?allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&useSSL=false
    spring.datasource.username=${DB_USERNAME:root}
    spring.datasource.password=${DB_PASSWORD:password}
### Install java: 
    sudo apt install openjdk-17* -y
    Java --version
### build backend:
    Change to backend directory: cd lms-be/
    ./mvnw clean package
- you will get target folder
- For manually running the application:
    java -jar target/LMS-0.0.1-SNAPSHOT.jar
### Backend service setup:
- backend service file to run the application in background
    sudo vi /etc/systemd/system/lms-be.service
        [Unit]
        Description=Your Spring Boot Application
        [Service]
        User=ubuntu
        ExecStart=/usr/bin/java -jar /home/ubuntu/lms-team-1/LMS-BE/target/LMS-0.0.1-SNAPSHOT.jar
        SuccessExitStatus=143
        [Install]
        WantedBy=multi-user.target
    sudo systemctl daemon-reload
    sudo systemctl start lms-be
    sudo systemctl enable lms-be
##### Check backend in browser: http://pub-ip:8080/user/login

## FRONTEND Server setup:

### Install nginx: sudo apt install nginx -y

### Install nodejs version: 20
    sudo apt-get update
**Visit: https://deb.nodesource.com/**
    sudo apt-get update && sudo apt-get install -y ca-certificates curl gnupg
    curl -fsSL https://deb.nodesource.com/gpgkey/nodesource-repo.gpg.key | sudo gpg --dearmor -o /etc/apt/keyrings/nodesource.gpg
    NODE_MAJOR=20
    echo "deb [signed-by=/etc/apt/keyrings/nodesource.gpg] https://deb.nodesource.com/node_$NODE_MAJOR.x nodistro main" | sudo tee /etc/apt/sources.list.d/nodesource.list
    sudo apt-get update
    sudo apt-get install nodejs -y
    sudo apt install nodejs -y
### Install node version manager (nvm) for installing node v:20

    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.0/install.sh | bash
    source ~/.bashrc
    nvm install 20
    nvm use 20 -----for setting node v.20 as default
## Connect to backend with frontend: 
    cd lms-fe/lmsv1/src/Components/
    sudo vi Home.jsx
    line-10: const response = await axios.get("**http://localhost:8080/user/login**");
### build frontend:
    Change to frontend directory: cd lms-fe/
    npm install
    npm run build – - - - - -you will get  build directory
### Now host your artifacts in nginx root directory
    sudo rm -f /var/www/html/*
    sudo cp -rf build/* /var/www/html/
    sudo systemctl restart nginx
    sudo systemctl status nginx
##### Check frontend in browser: http://pub-ip:80
----------------------------------------------------------------------------------------------------------------------------------
# LMS-JAVA Application Docker Deployment
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
- we will give database details while running the server
- docker run -d --name be --link mysql:mysql -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USERNAME=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 lmsbe
##### Check backend in browser: http://pub-ip:8080/user/login

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
##### Check frontend in browser: http://pub-ip:80


