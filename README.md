# DevOps CI/CD Demo Project

[![CI/CD Pipeline](https://github.com/jiya-singhal/devops-ci-cd/actions/workflows/ci.yml/badge.svg)](https://github.com/jiya-singhal/devops-ci-cd/actions/workflows/ci.yml)

A production-grade CI/CD pipeline demonstration using GitHub Actions, showcasing DevSecOps principles and best practices.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Pipeline Stages](#pipeline-stages)
- [Prerequisites](#prerequisites)
- [Local Setup](#local-setup)
- [GitHub Setup](#github-setup)
- [Running the Pipeline](#running-the-pipeline)
- [Security Features](#security-features)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

This project demonstrates a complete CI/CD pipeline that includes:

- ✅ Automated builds on every push
- ✅ Code quality checks (Checkstyle linting)
- ✅ Unit and integration testing
- ✅ Static Application Security Testing (SAST) with CodeQL
- ✅ Software Composition Analysis (SCA) with OWASP Dependency Check
- ✅ Docker containerization
- ✅ Container vulnerability scanning with Trivy
- ✅ Runtime validation
- ✅ Automated deployment to Docker Hub

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        CI/CD PIPELINE FLOW                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    │
│   │  Code    │───▶│  Build   │───▶│ Security │───▶│  Docker  │    │
│   │  Push    │    │  & Test  │    │  Scans   │    │  Build   │    │
│   └──────────┘    └──────────┘    └──────────┘    └──────────┘    │
│                                          │              │          │
│                                          ▼              ▼          │
│                                   ┌──────────┐    ┌──────────┐    │
│                                   │   SAST   │    │  Image   │    │
│                                   │ (CodeQL) │    │   Scan   │    │
│                                   └──────────┘    └──────────┘    │
│                                          │              │          │
│                                   ┌──────────┐          │          │
│                                   │   SCA    │          │          │
│                                   │ (OWASP)  │          ▼          │
│                                   └──────────┘    ┌──────────┐    │
│                                                   │ Runtime  │    │
│                                                   │  Test    │    │
│                                                   └──────────┘    │
│                                                         │          │
│                                                         ▼          │
│                                                   ┌──────────┐    │
│                                                   │  Push to │    │
│                                                   │Docker Hub│    │
│                                                   └──────────┘    │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

## 🔄 Pipeline Stages

| Stage | Tool | Purpose | Why It Matters |
|-------|------|---------|----------------|
| **Checkout** | actions/checkout | Get source code | Foundation for all subsequent steps |
| **Setup Java** | actions/setup-java | Install JDK 17 | Required for Java compilation |
| **Linting** | Checkstyle | Code style enforcement | Prevents technical debt, ensures consistency |
| **Unit Tests** | JUnit 5 | Test business logic | Catches bugs early, prevents regressions |
| **Build** | Maven | Package application | Creates deployable artifact (JAR) |
| **SAST** | CodeQL | Source code security scan | Finds OWASP Top 10 vulnerabilities in code |
| **SCA** | OWASP Dependency Check | Dependency scanning | Identifies vulnerable libraries |
| **Docker Build** | Docker | Create container image | Consistent, portable deployment |
| **Image Scan** | Trivy | Container vulnerability scan | Finds OS/library vulnerabilities in image |
| **Runtime Test** | curl | Smoke testing | Verifies container runs correctly |
| **Push** | docker/login-action | Publish to registry | Enables deployment |

## 📦 Prerequisites

### Local Development
- Java 17 or higher
- Maven 3.6+
- Docker Desktop
- Git

### GitHub
- GitHub account
- Docker Hub account

## 💻 Local Setup

### 1. Clone the Repository

```bash
git clone https://github.com/jiya-singhal/devops-ci-cd.git
cd devops-ci-cd
```

### 2. Build the Application

```bash
# Compile and run tests
mvn clean verify

# Package without tests (faster)
mvn package -DskipTests
```

### 3. Run Locally

```bash
# Run with Maven
mvn spring-boot:run

# Or run the JAR directly
java -jar target/devops-cicd-demo-1.0.0.jar
```

### 4. Test the Endpoints

```bash
# Health check
curl http://localhost:8080/health

# Hello endpoint
curl http://localhost:8080/hello
curl "http://localhost:8080/hello?name=YourName"

# Version
curl http://localhost:8080/version
```

### 5. Build and Run with Docker

```bash
# Build image
docker build -t devops-cicd-demo .

# Run container
docker run -p 8080:8080 devops-cicd-demo

# Test
curl http://localhost:8080/health
```

## ⚙️ GitHub Setup

### 1. Create GitHub Repository

1. Go to [GitHub](https://github.com) and create a new repository
2. Name it `devops-ci-cd`
3. Make it public (required for free GitHub Actions minutes)

### 2. Configure Secrets

**This is CRITICAL for the pipeline to work!**

1. Go to your repository → Settings → Secrets and variables → Actions
2. Click "New repository secret"
3. Add these secrets:

| Secret Name | Value | Description |
|-------------|-------|-------------|
| `DOCKERHUB_USERNAME` | Your Docker Hub username | Used to push images |
| `DOCKERHUB_TOKEN` | Your Docker Hub access token | Secure authentication |

**How to get Docker Hub Token:**
1. Log in to [Docker Hub](https://hub.docker.com)
2. Go to Account Settings → Security
3. Click "New Access Token"
4. Name it "GitHub Actions"
5. Copy the token (you won't see it again!)

### 3. Push Code

```bash
# Initialize git (if not already done)
git init

# Add remote
git remote add origin https://github.com/jiya-singhal/devops-ci-cd.git

# Add all files
git add .

# Commit
git commit -m "Initial commit: DevOps CI/CD demo project"

# Push to GitHub
git push -u origin master
```

## 🚀 Running the Pipeline

### Automatic Triggers
The pipeline runs automatically on:
- Push to `master` or `main` branch
- Pull requests to `master` or `main`

### Manual Trigger
1. Go to Actions tab in your repository
2. Select "CI/CD Pipeline" workflow
3. Click "Run workflow"
4. Select branch and click "Run workflow"

### Viewing Results
1. Go to Actions tab
2. Click on the workflow run
3. Click on individual jobs to see logs
4. Check Security tab for CodeQL results

## 🔒 Security Features

### SAST (Static Application Security Testing)
- **Tool:** GitHub CodeQL
- **What it does:** Analyzes source code for security vulnerabilities
- **Checks for:** SQL injection, XSS, path traversal, etc.
- **Results:** Visible in GitHub Security tab

### SCA (Software Composition Analysis)
- **Tool:** OWASP Dependency Check
- **What it does:** Scans dependencies for known vulnerabilities
- **Database:** National Vulnerability Database (NVD)
- **Report:** Uploaded as artifact

### Container Scanning
- **Tool:** Trivy
- **What it does:** Scans Docker image for vulnerabilities
- **Checks:** OS packages, application libraries
- **Severity:** Reports CRITICAL and HIGH

## 🔧 Troubleshooting

### Pipeline Fails at Build Stage
```bash
# Check locally
mvn clean compile

# Common issues:
# - Java version mismatch
# - Missing dependencies
```

### Checkstyle Failures
```bash
# Run checkstyle locally
mvn checkstyle:check

# Fix reported issues in your code
```

### Test Failures
```bash
# Run tests locally
mvn test

# Check test output for details
```

### Docker Build Fails
```bash
# Build locally to see errors
docker build -t test .

# Common issues:
# - Dockerfile syntax errors
# - Missing files
```

### Docker Push Fails
- Verify `DOCKERHUB_USERNAME` secret is set correctly
- Verify `DOCKERHUB_TOKEN` secret is valid
- Check Docker Hub account is active

### CodeQL Fails
- Ensure repository has Java code
- Check Java version compatibility
- Review CodeQL logs for specific errors

## 📚 Project Structure

```
devops-cicd-demo/
├── .github/
│   └── workflows/
│       └── ci.yml              # CI/CD pipeline definition
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── DemoApplication.java      # Main application
│   │   │       ├── HelloController.java      # REST endpoints
│   │   │       └── CalculatorService.java    # Business logic
│   │   └── resources/
│   │       └── application.properties        # Configuration
│   └── test/
│       └── java/
│           └── com/example/demo/
│               ├── CalculatorServiceTest.java    # Unit tests
│               └── HelloControllerTest.java      # Integration tests
├── Dockerfile                  # Container definition
├── .dockerignore              # Docker build exclusions
├── pom.xml                    # Maven configuration
├── checkstyle.xml             # Code style rules
├── dependency-check-suppression.xml  # SCA suppressions
├── .gitignore                 # Git exclusions
└── README.md                  # This file
```

## 🎓 Learning Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Reference](https://spring.io/projects/spring-boot)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)
- [DevSecOps Best Practices](https://www.devsecops.org/)

## 📝 License

This project is for educational purposes as part of the DevOps CI/CD assessment.

---

**Author:** Jiya Singhal  
**Student ID:** 10043  
**Date:** January 2026
