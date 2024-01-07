output "public_ip" {
  value = aws_instance.my_ec2_instance.public_ip
}

output "public_dns" {
  value = aws_instance.my_ec2_instance.public_dns
}

# output "instance_username" {
#   description = "Username of the EC2 instance"
#   value       = aws_instance.my_ec2_instance.key_name == "ubuntu" ? "ubuntu" : "ec2-user"
# }
