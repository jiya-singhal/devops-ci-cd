# DevOps CI/CD Demo Project

A complete CI/CD pipeline with Kubernetes deployment using GitHub Actions.

---

## What This Project Does

This project demonstrates a production-style CI/CD pipeline that:

- Builds a Java Spring Boot application
- Runs unit tests automatically
- Checks code style with Checkstyle
- Scans for security vulnerabilities (SAST with CodeQL)
- Scans dependencies for known CVEs (SCA with OWASP)
- Builds and scans Docker images (Trivy)
- Pushes verified images to Docker Hub
- Deploys to a Kubernetes cluster

---
Step 1 - Code Push: When I push code to GitHub, CI pipeline triggers automatically.
Step 2 - Build & Test: It sets up Java 17, runs Checkstyle to check code formatting, runs 12 unit tests using JUnit, and builds a JAR file using Maven.
Step 3 - Security Scanning: Three types of security scans run:
* SAST using CodeQL - scans my source code for vulnerabilities like SQL injection
* SCA using OWASP Dependency Check - scans my dependencies for known CVEs
* Container scanning using Trivy - scans the Docker image
Step 4 - Docker: It builds a Docker image using multi-stage build to keep size small (~180MB), tests the container by hitting /health endpoint, then pushes to Docker Hub.
Step 5 - CD Pipeline: After CI passes, CD pipeline starts. It uses Terraform to create AWS infrastructure - an EC2 t3.small instance with Security Group.
Step 6 - Kubernetes: The EC2 runs a user_data script that installs k3s (lightweight Kubernetes), pulls my Docker image, creates a Deployment with 2 replicas, and exposes it using NodePort service.
Step 7 - Result: My app becomes live at http://EC2-IP:30821. Anyone on the internet can access it.
This is DevSecOps because security is integrated at every stage, not just at the end.

## Requirements

- Java 17
- Maven
- Docker
- GitHub account
- Docker Hub account

---

## Running Locally

```bash
# Clone the repo
git clone https://github.com/jiya-singhal/devops-ci-cd.git
cd devops-ci-cd

# Build and test
mvn clean verify

# Run the application
mvn spring-boot:run

# Test the endpoints
curl http://localhost:8080/health    # Returns: OK
curl http://localhost:8080/hello     # Returns: Hello, World!
curl http://localhost:8080/version   # Returns: 1.0.0
```

---

## Running with Docker

```bash
# Build the image
docker build -t devops-ci-cd .

# Run the container
docker run -p 8080:8080 devops-ci-cd

# Test
curl http://localhost:8080/health
```

---

## GitHub Secrets Configuration

Go to your repo → Settings → Secrets and variables → Actions → New repository secret

Add these two secrets:

| Secret Name | Value | How to Get It |
|-------------|-------|---------------|
| `DOCKERHUB_USERNAME` | Your Docker Hub username | Your login username |
| `DOCKERHUB_TOKEN` | Docker Hub access token | Docker Hub → Account Settings → Security → New Access Token |

**Important:** Never hardcode these values in your code!

---

## CI/CD Pipeline Explanation

The pipeline is defined in `.github/workflows/ci.yml` and runs automatically on every push to main/master.

### Pipeline Stages:

```
┌─────────────────────────────────────────────────────────────────┐
│                         CI STAGES                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  1. BUILD & TEST                                                │
│     └─ Checkout → Setup Java → Lint → Test → Build JAR         │
│                                                                 │
│  2. SECURITY SCANS (run in parallel)                           │
│     ├─ SAST: CodeQL scans source code                          │
│     └─ SCA: OWASP checks dependencies for CVEs                 │
│                                                                 │
│  3. DOCKER                                                      │
│     └─ Build Image → Trivy Scan → Test Container → Push        │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                         CD STAGES                                │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│  4. KUBERNETES DEPLOYMENT                                       │
│     └─ Start Minikube → Deploy App → Create Service → Verify   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

Code Push
    ↓
┌─────────────────────────────────────┐
│           CI PIPELINE               │
├─────────────────────────────────────┤
│ Checkout → Java Setup → Checkstyle  │
│     ↓                               │
│ Unit Tests (12 tests)               │
│     ↓                               │
│ Build JAR (Maven)                   │
│     ↓                               │
│ ┌─────────┐  ┌─────────┐           │
│ │  SAST   │  │   SCA   │ (parallel)│
│ │ CodeQL  │  │  OWASP  │           │
│ └─────────┘  └─────────┘           │
│     ↓                               │
│ Docker Build → Trivy Scan → Push   │
└─────────────────────────────────────┘
    ↓
┌─────────────────────────────────────┐
│           CD PIPELINE               │
├─────────────────────────────────────┤
│ Terraform → Create EC2 + SG        │
│     ↓                               │
│ Install k3s (Kubernetes)           │
│     ↓                               │
│ Deploy App (2 replicas)            │
│     ↓                               │
│ Expose via NodePort (30821)        │
└─────────────────────────────────────┘
    ↓
App Live at http://EC2-IP:30821

### What Each Stage Does:

| Stage | Tool | Purpose |
|-------|------|---------|
| Checkout | actions/checkout | Downloads code from GitHub |
| Setup Java | actions/setup-java | Installs Java 17 |
| Linting | Checkstyle | Enforces code style rules |
| Unit Tests | JUnit 5 | Verifies code works correctly |
| Build | Maven | Creates JAR file |
| SAST | CodeQL | Finds security bugs in code |
| SCA | OWASP Dependency Check | Finds vulnerable libraries |
| Docker Build | Docker | Creates container image |
| Image Scan | Trivy | Finds vulnerabilities in container |
| Container Test | curl | Verifies container runs properly |
| Push | Docker Hub | Publishes verified image |
| K8s Deploy | Minikube + kubectl | Deploys to Kubernetes cluster |

### Why This Order?

- Fast checks run first (fail-fast principle)
- Security scans run before Docker push
- Only verified images get deployed

---

## Kubernetes Deployment

The CD stage deploys the application to a Kubernetes cluster using Minikube.

### K8s Files:

- `k8s/deployment.yaml` - Defines how to run the app (2 replicas)
- `k8s/service.yaml` - Exposes the app on a NodePort

### What Happens:

1. Minikube starts a local K8s cluster
2. kubectl applies the deployment (creates 2 pods)
3. kubectl applies the service (exposes on port 30080)
4. Pipeline verifies the deployment is healthy

### Running K8s Locally (optional):

```bash
# Start minikube
minikube start

# Deploy
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Check status
kubectl get pods
kubectl get services

# Access the app
minikube service devops-demo-service --url
```

---

## Project Structure

```
devops-ci-cd/
├── .github/
│   └── workflows/
│       └── ci.yml              # CI/CD pipeline
├── k8s/
│   ├── deployment.yaml         # Kubernetes deployment
│   └── service.yaml            # Kubernetes service
├── src/
│   ├── main/java/              # Application code
│   └── test/java/              # Unit tests
├── Dockerfile                  # Container build instructions
├── pom.xml                     # Maven config and dependencies
├── checkstyle.xml              # Code style rules
└── README.md
```

---

## Triggering the Pipeline

**Automatic:** Push to main or master branch

**Manual:** Go to Actions tab → Select workflow → Run workflow

---

## Viewing Results

- **Pipeline logs:** GitHub repo → Actions tab
- **Security findings:** GitHub repo → Security tab
- **Docker image:** Docker Hub → Your repositories

---

Jiya Singhal

Roll No: 10043 