# AWS Provider
terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Variables
variable "aws_region" {
  description = "AWS region"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "EC2 instance type"
  type        = string
  default     = "t3.small"  # Free tier - 2GB RAM - good for k3s!
}

variable "ssh_public_key" {
  description = "SSH public key for EC2 access"
  type        = string
}

variable "dockerhub_username" {
  description = "Docker Hub username"
  type        = string
}

variable "image_name" {
  description = "Docker image name"
  type        = string
}

# Get latest Ubuntu 22.04 AMI
data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

# Security Group for K8s
resource "aws_security_group" "k8s_sg" {
  name        = "devops-k8s-sg"
  description = "Security group for DevOps K8s demo"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 6443
    to_port     = 6443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 30000
    to_port     = 32767
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "devops-k8s-sg"
  }
}

# Key Pair
resource "aws_key_pair" "deployer" {
  key_name   = "devops-k8s-key"
  public_key = var.ssh_public_key

  tags = {
    Name = "devops-k8s-key"
  }
}

# EC2 Instance with k3s
resource "aws_instance" "k8s_server" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  key_name               = aws_key_pair.deployer.key_name
  vpc_security_group_ids = [aws_security_group.k8s_sg.id]

  root_block_device {
    volume_size = 20
    volume_type = "gp2"
  }

  user_data = <<-EOF
              #!/bin/bash
              set -e
              exec > >(tee /var/log/user-data.log) 2>&1
              echo "Starting setup at $(date)"
              
              # Update system
              apt-get update
              apt-get install -y curl
              
              # Install k3s with reduced footprint for 2GB RAM
              echo "Installing k3s..."
              curl -sfL https://get.k3s.io | sh -s - \
                --write-kubeconfig-mode 644 \
                --disable traefik \
                --disable metrics-server
              
              # Wait for k3s to be ready
              echo "Waiting for k3s..."
              sleep 60
              
              # Wait for node ready
              for i in {1..30}; do
                if kubectl get nodes 2>/dev/null | grep -q "Ready"; then
                  echo "Node is ready!"
                  break
                fi
                echo "Waiting for node... attempt $i"
                sleep 10
              done
              
              # Create deployment with 2 replicas
              echo "Creating deployment..."
              cat <<DEPLOY | kubectl apply -f -
              apiVersion: apps/v1
              kind: Deployment
              metadata:
                name: devops-app
              spec:
                replicas: 2
                selector:
                  matchLabels:
                    app: devops-app
                template:
                  metadata:
                    labels:
                      app: devops-app
                  spec:
                    containers:
                      - name: devops-app
                        image: ${var.dockerhub_username}/${var.image_name}:latest
                        ports:
                          - containerPort: 8080
                        resources:
                          requests:
                            memory: "256Mi"
                            cpu: "100m"
                          limits:
                            memory: "512Mi"
                            cpu: "300m"
                        livenessProbe:
                          httpGet:
                            path: /health
                            port: 8080
                          initialDelaySeconds: 30
                          periodSeconds: 10
                        readinessProbe:
                          httpGet:
                            path: /health
                            port: 8080
                          initialDelaySeconds: 5
                          periodSeconds: 5
              DEPLOY
              
              # Wait for deployment
              echo "Waiting for deployment..."
              kubectl rollout status deployment/devops-app --timeout=180s || true
              
              # Create NodePort service
              echo "Creating service..."
              kubectl expose deployment devops-app \
                --type=NodePort \
                --port=8080 \
                --target-port=8080 \
                --name=devops-service || true
              
              # Save status
              echo "=== Setup Complete ===" > /home/ubuntu/deployment-status.txt
              date >> /home/ubuntu/deployment-status.txt
              echo "" >> /home/ubuntu/deployment-status.txt
              echo "=== Nodes ===" >> /home/ubuntu/deployment-status.txt
              kubectl get nodes >> /home/ubuntu/deployment-status.txt 2>&1
              echo "" >> /home/ubuntu/deployment-status.txt
              echo "=== Pods ===" >> /home/ubuntu/deployment-status.txt
              kubectl get pods >> /home/ubuntu/deployment-status.txt 2>&1
              echo "" >> /home/ubuntu/deployment-status.txt
              echo "=== Services ===" >> /home/ubuntu/deployment-status.txt
              kubectl get svc >> /home/ubuntu/deployment-status.txt 2>&1
              
              echo "Setup complete at $(date)"
              EOF

  tags = {
    Name = "devops-k8s-server"
  }
}

# Outputs
output "instance_id" {
  value = aws_instance.k8s_server.id
}

output "instance_public_ip" {
  value = aws_instance.k8s_server.public_ip
}

output "ssh_command" {
  value = "ssh -i ~/.ssh/deployer ubuntu@${aws_instance.k8s_server.public_ip}"
}
