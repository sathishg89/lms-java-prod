resource "aws_security_group" "frontend_sg" {
  name        = "allow_all"
  description = "Allow all inbound and outbound traffic"

  vpc_id = "vpc-06082df6a4b78dea1"  # Replace with your VPC ID, you will get while creating VPC with terraform

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
    Name = "frontend-sg"
  }
}
