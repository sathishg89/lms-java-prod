resource "aws_instance" "backend" {
  ami           = "ami-0da7657fe73215c0c"  # Replace with your desired AMI ID, this ami-id: ubuntu-20.04
  instance_type = "t2.medium"
  key_name      = "unv-california"  # Replace with your key pair name

  root_block_device {
    volume_type           = "gp2"
    volume_size           = 15
    delete_on_termination = true
  }

  vpc_security_group_ids = [aws_security_group.backend_sg.id]
  subnet_id              = "subnet-02ced4604c2c029c0"         # Replace with your subnet ID, you will get while creating the Subnet using Terraform

  tags = {
    Name = "lms-backend"
  }

  user_data = file("${path.module}/backend.sh")
}
