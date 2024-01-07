# backend server output file
output "public_ip" {
  value = aws_instance.backend.public_ip
}

output "public_dns" {
  value = aws_instance.backend.public_dns
}
