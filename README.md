# LMS-JAVA MINIKUBE DEPLOYMENT
## STEPS:
- Launch Server
- Install Software
- Create K8S Manifest files
- Deploy K8S files

## STEP-1: Launch Server
- Guide - https://minikube.sigs.k8s.io/docs/start/
- Requirements —-------t2.medium instance in AWS
- 2 CPUs or more
- 2GB of free ram memory
- 30GB of free disk space

## STEP-2: Install Softwares
### Update system
- sudo apt update
### Docker setup
- Visit: https://get.docker.com/
- curl -fsSL https://get.docker.com -o install-docker.sh
- sudo sh install-docker.sh
- sudo usermod -aG docker ubuntu
- newgrp docker
- docker -v 
### Kubectl setup
- Visit: https://kubernetes.io/docs/tasks/tools/install-kubectl-linux/#install-kubectl-binary-with-curl-on-linux
- curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
- sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
- chmod +x kubectl
- sudo mv kubectl /usr/local/bin/kubectl
- kubectl version
### Minikube setup
- Visit: https://minikube.sigs.k8s.io/docs/start/
- curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
- sudo install minikube-linux-amd64 /usr/local/bin/minikube
- minikube version
- minikube status
- minikube start

## STEP-3: Create K8S Manifest files
- Code: git clone -b dev https://github.com/muralialakuntla3/lms-java.git

## STEP-4: Deploy K8S files
### Database:
- to encrypt the password
- **echo -n password | base64**
- **encrypted-pw**
#### mysql k8s deployment
- kubectl apply -f mysql-secret.yml
- kubectl apply -f mysql-deployment.yml
- kubectl apply -f mysql-cluster-ip.yml

### Docker login:
- generate PAT in docker hub
- My Account -> settings -> New Access Token
- login to your server
- docker login -u muralialakuntla3
- password: paste your token 
### Backend:
- cd LMS-BE
- chmod +x mvnw
- docker build -t muralialakuntla3/lms-java-be
- docker push muralialakuntla3/lms-java-be
- kubectl apply -f backend-configmap.yml
- kubectl apply -f backend-deployment.yml
- kubectl apply -f backend-service.yml

#### to check backend use port-forward cmd
- kubectl port-forward service/backend-service **32323:8080** --address 0.0.0.0
- check in browser: **pub-ip:32315/user/connect**
  

### Frontend:
#### Connect frontend with backend  : 
- cd LMS-FE/src/
- sudo vi utils.js
- export const url =("**http://pub-ip:8080/user/login**");
#### frontend deployment
- docker build -t muralialakuntla3/lms-java-be
- docker push muralialakuntla3/lms-java-be
- kubectl apply -f frontend-deployment.yml
- kubectl apply -f frontend-service.yml
#### to check backend use port-forward cmd
- kubectl port-forward service/frontend-service **30700:80** --address 0.0.0.0
- check in browser: **pub-ip:32315**



------------------------------------------------------------------------------------
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
- docker run -d --name mysql --network lmsnetwork -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 -e MYSQL_DATABASE=lmsdb mysql

## BACKEND setup:
- cd LMS-BE
- docker build -t lmsbe .
- docker run -d --name be --network lmsnetwork -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USER=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 lmsbe
### Check backend in browser
- browse : pub-ip:8080/user/login

## FRONTEND Server setup:

### Connect frontend with backend  : 
- cd LMS-FE/src/
- sudo vi utils.js
- export const url =("**http://pub-ip:8080/user/login**");

### build frontend:
- cd LMS-FE
- docker build -t lmsfe .
- docker run -d --name fe --network lmsnetwork -p 80:80 lmsfe
- docker ps
### Check frontend in browser
- browse : pub-ip:80
- click on **Test**
- it will show **Connected To Back-End**
