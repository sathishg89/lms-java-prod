resource "aws_instance" "backend" {
  ami           = "ami-0da7657fe73215c0c"  # Replace with your desired AMI ID
  instance_type = "t2.medium"
  key_name      = "unv-california"  # Replace with your key pair name

  root_block_device {
    volume_type           = "gp2"
    volume_size           = 15
    delete_on_termination = true
  }

  vpc_security_group_ids = [aws_security_group.allow_all.id]
  subnet_id             = aws_subnet.public_subnet.id

  tags = {
    Name = "lms-backend"
  }

  user_data = <<-EOF
              #!/bin/bash
              sudo apt-get update -y
              sudo apt-get install -y curl
              curl -fsSL https://get.docker.com -o get-docker.sh
              sudo sh get-docker.sh
              sudo usermod -aG docker $USER
              newgrp docker
              sudo docker network create -d bridge lmsnetwork
              sudo docker run -d --name mysql --network lmsnetwork -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 -e MYSQL_DATABASE=lmsdb mysql
              sudo docker run -d --name be --network lmsnetwork -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USERNAME=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 muralialakuntla3/terraform-be
              EOF
}
