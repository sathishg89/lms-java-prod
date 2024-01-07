# LMS deployment with Terraform
## launch server and install terraform
- visit: https://developer.hashicorp.com/terraform/install
- wget -O- https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
- echo "deb [signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
- sudo apt update && sudo apt install terraform
## install aws cli
- sudo apt  install awscli -y
- aws configure
- aws s3 ls
## deploy infra and application
- clone the code from this branch
- update provider.tf details
- terraform init
- terraform validate
- terraform plan
- terraform apply
- terraform destroy
