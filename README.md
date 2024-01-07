# LMS deployment with Terraform

## Launch server and install terraform
- visit: https://developer.hashicorp.com/terraform/install
- wget -O- https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
- echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
- sudo apt update && sudo apt install terraform
  
## Install aws cli
- sudo apt  install awscli -y
- aws configure
- aws s3 ls

# Deploy Infra and Application

## VPC creation
- git clone -b terraform https://github.com/muralialakuntla3/lms-java.git
- cd lms-java/terraform/
- sudo vi provider.tf    --> update your access and secret key details
- terraform init
- terraform validate
- terraform plan
- terraform apply

### To save terraform output details 
- terraform output -json > vpc_outputs.json
- cat vpc_outputs.json

## Backend deployment
- update vpc id in backend-firewall.tf   --> vpc-07f90316611fbbb50
- sudo vi backend-firewall.tf
- update subnet id in backend-server.tf  --> subnet-02ced4604c2c029c0
- sudo vi backend-server.tf
  
### Terraform Operations
- terraform init
- terraform validate
- terraform plan
- terraform apply

### To save terraform output details 
- terraform output -json > backend-output.json
- **Note:** wait few minutes to get changes in server
- browse pub-ip:8080/user

## Frontend deployment
- update backend url in frontend
- sudo vi lms-fe/lmsv1/src/Components/Home.jsx   --> 10th line 

- update vpc id in backend-firewall.tf   --> vpc-07f90316611fbbb50
- sudo vi frontend-firewall.tf
- update subnet id in frontend-server.tf  --> subnet-02ced4604c2c029c0
- sudo vi frontend-server.tf
  
### Terraform Operations
- terraform init
- terraform validate
- terraform plan
- terraform apply

### To save terraform output details 
- terraform output -json > frontend-output.json
- **Note:** wait few minutes to get changes in server
- browse pub-ip and click on Test --> Connect to Backend

## Deleting resources
- Delete Frontend
  - cd ~/lms-java/terraform/frontend
  - terraform destroy
    
- Delete Backend
  - cd ~/lms-java/terraform/backend
  - terraform destroy
    
- Delete VPC
  - cd ~/lms-java/terraform
  - terraform destroy



