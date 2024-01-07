resource "aws_instance" "backend" {
  ami             = "ami-0da7657fe73215c0c"  # replace with your desired AMI ID
  instance_type   = "t2.medium"
  key_name        = "unv-california"  # replace with your key pair name

  root_block_device {
    volume_type           = "gp2"
    volume_size           = 15
    delete_on_termination = true
  }

  vpc_security_group_ids = [aws_security_group.allow_all.id]  # reference to the security group created earlier
  subnet_id             = aws_subnet.public_subnet.id        # reference to the public subnet created earlier
  connection {
    type        = "ssh"
    user        = "ubuntu"  # Replace with your EC2 instance's user (Ubuntu default user is 'ubuntu')
    private_key = file("~/lms-java/terraform/unv-california.pem")  # Replace with your private key file path
    host        = self.public_ip  # If your instance has a public IP, else use 'private_ip'
  }

  provisioner "remote-exec" {
    inline = [
      "sudo apt update",
      "curl -fsSL https://get.docker.com -o install-docker.sh",
      "sudo sh install-docker.sh",
      "sudo usermod -aG docker $USER",
      "newgrp docker",
      "sudo docker network create -d bridge lmsnetwork",
      "sudo docker run -d --name mysql --network lmsnetwork -p 3306:3306 -e MYSQL_ROOT_PASSWORD=Qwerty@123 -e MYSQL_DATABASE=lmsdb mysql",
      "git clone -b terraform https://github.com/muralialakuntla3/lms-java.git",
      "cd ~/lms-java/LMS-BE",
      "sudo docker build -t lmsbe .",
      "sudo docker run -d --name be --network lmsnetwork -e DB_HOST=mysql -e DB_PORT=3306 -e DB_NAME=lmsdb -e DB_USER=root -e DB_PASSWORD=Qwerty@123 -p 8080:8080 lmsbe",
    ]
  }
  tags = {
    Name = "lms-backend"
  }
}

