# this file is used to print vpc-id and subnet-id, we will use them in firewall and server file

output "vpc_id" {
  value = aws_vpc.lms_vpc.id
}

output "subnet_id" {
  value = aws_subnet.lms_public_subnet.id
}
