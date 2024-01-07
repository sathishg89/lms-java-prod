resource "aws_security_group" "backend_sg" {
  name        = "allow_all_traffic"
  description = "Allow all inbound and outbound traffic"

  vpc_id = "vps-sfgh578y6terugh432"  # Replace with your VPC ID , you will get while creating the VPC with terraform

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
  tags = {
    Name = "backend-sg"
  }
}
