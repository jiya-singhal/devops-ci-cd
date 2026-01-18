# DevOps CI/CD Demo Project

A CI/CD pipeline project using GitHub Actions.

## What This Project Does

- Builds a Java Spring Boot app
- Runs tests
- Checks code style with Checkstyle
- Scans for security issues (CodeQL, OWASP Dependency Check)
- Builds a Docker image
- Scans the image with Trivy
- Pushes to Docker Hub

## Requirements

- Java 17
- Maven
- Docker
- GitHub and Docker Hub accounts

## Running Locally

```bash
# Clone
git clone https://github.com/jiya-singhal/devops-ci-cd.git
cd devops-ci-cd

# Build
mvn clean verify

# Run
mvn spring-boot:run

# Test endpoints
curl http://localhost:8080/health
curl http://localhost:8080/hello
```

## Docker

```bash
docker build -t devops-cicd-demo .
docker run -p 8080:8080 devops-cicd-demo
```

## GitHub Setup

Add these secrets in your repo settings (Settings > Secrets and variables > Actions):

- `DOCKERHUB_USERNAME` - your Docker Hub username
- `DOCKERHUB_TOKEN` - access token from Docker Hub (Account Settings > Security > New Access Token)

## Pipeline

The pipeline runs on push to main/master. You can also trigger it manually from the Actions tab.

## Project Structure

```
├── .github/workflows/ci.yml    # pipeline config
├── src/main/java/              # application code
├── src/test/java/              # tests
├── Dockerfile
├── pom.xml
└── checkstyle.xml
```

---

Jiya Singhal - 10043
