# LMS-JAVA Application VM Deployment
## Server setup:
    Server type: T2.medium server
    Database: mysql 
    Backend: java-17
    Frontend: node-20
    Ports: 22,80,8080,3306

### Repo link: https://github.com/muralialakuntla3/lms-spring.git

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
    spring.datasource.url=jdbc:mysql://**localhost:3306**/LMS?allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true&useSSL=false
    spring.datasource.username=**root**
    spring.datasource.password=**password**
### Install java: 
    sudo apt install openjdk-17* -y
    Java --version
### build backend:
    Change to backend directory: cd lms-be/
    chmod +x mvnw
    ./mvnw package
	
    For checking: java -jar target/LMS-0.0.1-SNAPSHOT.jar
### Backend service setup:
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
### build frontend:
    Change to frontend directory: cd lms-fe/
    npm install
    npm run build – - - - - -you will get  build directory
### Now host your artifacts in nginx root directory

    sudo rm -f /var/www/html/*
    sudo cp -rf build/* /var/www/html/
    sudo systemctl restart nginx
    sudo systemctl status nginx

## Connect to backend with frontend: 
    cd lms-fe/lmsv1/src/Components/
    sudo vi Home.jsx
   ** line-10:** const response = await axios.get("**http://localhost:8080/user/login**");


