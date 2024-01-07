resource "aws_instance" "frontend" {
  ami           = "ami-0da7657fe73215c0c"  # Replace with your desired AMI ID
  instance_type = "t2.medium"
  key_name      = "unv-california"  # Replace with your key pair name

  root_block_device {
    volume_type           = "gp2"
    volume_size           = 15
    delete_on_termination = true
  }

  vpc_security_group_ids = [aws_security_group.frontend_sg.id]
  subnet_id             = "subnet-08ba2b0d023fa5445"             # Replace with your subnet ID, you will get while creating the Subnet using Terraform

  tags = {
    Name = "lms-frontend"
  }

  user_data = <<-EOF
              #!/bin/bash
              sudo apt-get update -y
              sudo apt-get install -y curl git

              sudo curl -fsSL https://get.docker.com -o get-docker.sh
              sudo sh get-docker.sh
              sudo usermod -aG docker ubuntu  # Add ubuntu user to docker group

              sudo git clone -b terraform https://github.com/muralialakuntla3/lms-java.git /home/ubuntu/lms-java
              sudo chown -R ubuntu:ubuntu /home/ubuntu/lms-java  # Change ownership to ubuntu user

              sudo docker network create -d bridge lmsnetwork
              sudo docker build -t muralialakuntla3/terraform-fe /home/ubuntu/lms-java/lms-fe/lmsv1
              sudo docker run -d --name fe --network lmsnetwork -p 80:80 muralialakuntla3/terraform-fe
              echo "Frontend Docker deployment completed" > /var/log/user-data.log 2>&1
              EOF
}
