# 🎯 VIVA PREPARATION GUIDE
## DevOps CI/CD Project - Complete Question Bank with Answers

> **IMPORTANT**: This guide covers 40% of your marks. Study it thoroughly!

---

## 📚 TABLE OF CONTENTS

1. [Basic Concepts](#1-basic-concepts)
2. [Your Pipeline Specific Questions](#2-your-pipeline-specific-questions)
3. [Security Questions (DevSecOps)](#3-security-questions-devsecops)
4. [Docker & Containerization](#4-docker--containerization)
5. [GitHub Actions Deep Dive](#5-github-actions-deep-dive)
6. [Troubleshooting Scenarios](#6-troubleshooting-scenarios)
7. [Advanced/Tricky Questions](#7-advancedtricky-questions)

---

## 1. BASIC CONCEPTS

### Q: What is CI/CD? Explain in simple terms.
**Answer:**
- **CI (Continuous Integration)**: Automatically building and testing code every time a developer pushes changes. It catches bugs early.
- **CD (Continuous Delivery)**: Automatically preparing code for release to production after passing all tests.
- **CD (Continuous Deployment)**: Automatically deploying to production without manual intervention.

**Simple analogy**: CI/CD is like a factory assembly line with quality checkpoints. Each checkpoint must pass before the product moves forward.

---

### Q: What is DevOps? How is it different from traditional development?
**Answer:**
| Traditional | DevOps |
|------------|--------|
| Dev and Ops are separate teams | Dev and Ops work together |
| Manual deployments | Automated deployments |
| Long release cycles (months) | Short cycles (days/hours) |
| Problems found in production | Problems caught early |
| Blame culture | Shared responsibility |

DevOps is a **culture + practices + tools** that increases the ability to deliver applications faster.

---

### Q: What is "Shift-Left"? Why is it important?
**Answer:**
"Shift-Left" means moving testing and security checks **earlier** in the development lifecycle (to the "left" on a timeline).

**Why it matters:**
- Bugs found in development cost **$10** to fix
- Same bugs found in production cost **$10,000** to fix
- Security vulnerabilities caught early = less damage

**In our pipeline**: We run linting first (catches style issues), then tests (catches bugs), then security scans (catches vulnerabilities) - all BEFORE deployment.

---

### Q: What is a "Quality Gate"?
**Answer:**
A quality gate is a **checkpoint** that code must pass before moving to the next stage.

**In our pipeline, we have these quality gates:**
1. Checkstyle must pass (no code style violations)
2. All unit tests must pass
3. SAST must complete (CodeQL analysis)
4. SCA must complete (dependency check)
5. Container must start and respond to health check

**If ANY gate fails, the pipeline STOPS.** This ensures only quality code reaches production.

---

## 2. YOUR PIPELINE SPECIFIC QUESTIONS

### Q: Walk me through your pipeline stages. Why is each stage there?
**Answer:**

```
1. CHECKOUT
   - Gets source code from GitHub
   - Without this, we have nothing to build!

2. SETUP JAVA
   - Installs Java 17 JDK
   - Uses Maven caching for faster builds

3. LINTING (Checkstyle)
   - Enforces coding standards
   - Catches issues like: unused imports, naming violations
   - WHY: Prevents technical debt

4. UNIT TESTS (JUnit)
   - Tests our business logic
   - WHY: Prevents regressions, catches bugs early

5. BUILD (Maven)
   - Compiles code, creates JAR file
   - WHY: Creates the deployable artifact

6. SAST (CodeQL)
   - Static Application Security Testing
   - Scans SOURCE CODE for vulnerabilities
   - WHY: Finds OWASP Top 10 issues like SQL injection

7. SCA (OWASP Dependency Check)
   - Software Composition Analysis
   - Scans DEPENDENCIES (libraries) for CVEs
   - WHY: Supply chain security

8. DOCKER BUILD
   - Creates container image
   - Uses multi-stage build for smaller image
   - WHY: Consistent deployment environment

9. IMAGE SCAN (Trivy)
   - Scans container for vulnerabilities
   - Checks OS packages and libraries
   - WHY: Last security check before deployment

10. RUNTIME TEST
    - Actually runs the container
    - Tests health endpoint
    - WHY: Ensures it actually works!

11. DOCKER PUSH
    - Pushes to Docker Hub
    - WHY: Makes image available for deployment
```

---

### Q: Why is the order of stages important?
**Answer:**
The order follows these principles:

1. **Fail-Fast**: Quick checks run first (linting is faster than security scans)
2. **Dependencies**: Some stages need previous stage outputs (Docker build needs JAR)
3. **Cost**: Expensive operations (security scans) run only if cheap checks pass
4. **Security Gate**: Security scans before Docker push = no vulnerable images published

**Example**: We run Checkstyle (2 seconds) before CodeQL (5 minutes). If linting fails, we don't waste time on security scans.

---

### Q: What triggers your pipeline?
**Answer:**
```yaml
on:
  push:
    branches: [master, main]  # Runs on push to main branches
  pull_request:
    branches: [master, main]  # Runs on PRs to main branches
  workflow_dispatch:          # Manual trigger from GitHub UI
```

**Why these triggers:**
- `push`: Validates every code change
- `pull_request`: Validates PRs before merge
- `workflow_dispatch`: Allows manual re-runs for debugging

---

### Q: What happens if a stage fails?
**Answer:**
- The pipeline **STOPS** immediately (fail-fast)
- Subsequent stages don't run
- The developer gets a **notification** (email/GitHub)
- The commit is marked as **failed** (red X)
- The image is **NOT pushed** to Docker Hub

**This is intentional!** We don't want broken/insecure code to proceed.

---

## 3. SECURITY QUESTIONS (DevSecOps)

### Q: What is SAST? What tool do you use?
**Answer:**
**SAST = Static Application Security Testing**

- Analyzes **source code** without running it
- Tool: **GitHub CodeQL**
- How it works:
  1. Builds a database of your code
  2. Runs queries against it for vulnerability patterns
  3. Reports findings in GitHub Security tab

**What it catches:**
- SQL Injection
- Cross-Site Scripting (XSS)
- Path Traversal
- Hardcoded credentials

---

### Q: What is SCA? What tool do you use?
**Answer:**
**SCA = Software Composition Analysis**

- Scans **dependencies** (third-party libraries)
- Tool: **OWASP Dependency Check**
- How it works:
  1. Identifies all dependencies in pom.xml
  2. Checks against NVD (National Vulnerability Database)
  3. Reports known CVEs

**Why it matters:**
- 80% of code in modern apps is from libraries
- Libraries have known vulnerabilities (CVEs)
- Log4Shell (CVE-2021-44228) affected millions of apps

---

### Q: What is Trivy? Why scan containers?
**Answer:**
**Trivy** is a container vulnerability scanner.

**Why scan containers:**
- Base images (like `eclipse-temurin:17-jre-alpine`) have OS packages
- These packages can have vulnerabilities
- Even if YOUR code is secure, the container might not be

**What Trivy checks:**
- OS packages (Alpine, Debian packages)
- Application dependencies
- Known CVEs in all layers

---

### Q: What is the OWASP Top 10?
**Answer:**
OWASP Top 10 is a list of the most critical web application security risks:

1. **A01: Broken Access Control** - Users accessing unauthorized data
2. **A02: Cryptographic Failures** - Weak encryption
3. **A03: Injection** - SQL injection, XSS
4. **A04: Insecure Design** - Flawed architecture
5. **A05: Security Misconfiguration** - Default passwords
6. **A06: Vulnerable Components** - Outdated libraries (SCA catches this!)
7. **A07: Auth Failures** - Weak authentication
8. **A08: Data Integrity Failures** - Trusting untrusted data
9. **A09: Logging Failures** - Not detecting attacks
10. **A10: SSRF** - Server-Side Request Forgery

**Our pipeline catches:** A03 (CodeQL), A06 (OWASP DC + Trivy)

---

### Q: What is a CVE?
**Answer:**
**CVE = Common Vulnerabilities and Exposures**

- A standardized identifier for security vulnerabilities
- Format: CVE-YEAR-NUMBER (e.g., CVE-2021-44228)
- Managed by MITRE Corporation
- Has severity scores (CVSS): 0-10

**Example:**
- CVE-2021-44228 = Log4Shell
- CVSS: 10.0 (Critical)
- Affected millions of Java applications

---

## 4. DOCKER & CONTAINERIZATION

### Q: What is Docker? Why use it?
**Answer:**
Docker is a **containerization platform** that packages applications with all dependencies.

**Why use Docker:**
1. **Consistency**: "Works on my machine" → "Works everywhere"
2. **Isolation**: App runs in its own environment
3. **Portability**: Run anywhere (laptop, cloud, server)
4. **Scalability**: Easy to scale with orchestrators

**Container vs VM:**
| Container | Virtual Machine |
|-----------|-----------------|
| Shares host OS kernel | Full OS per VM |
| Lightweight (MBs) | Heavy (GBs) |
| Starts in seconds | Starts in minutes |
| Less isolation | Full isolation |

---

### Q: Explain your Dockerfile. What is multi-stage build?
**Answer:**
```dockerfile
# STAGE 1: BUILD (has Maven, JDK - big image)
FROM maven:3.9-eclipse-temurin-17 AS builder
# ... build the JAR ...

# STAGE 2: RUN (only JRE - small image)
FROM eclipse-temurin:17-jre-alpine
COPY --from=builder /app/target/*.jar app.jar
```

**Multi-stage build benefits:**
1. **Smaller images**: Final image doesn't have Maven, source code
2. **Security**: Fewer packages = fewer vulnerabilities
3. **Faster pulls**: Smaller images download faster

**Our image sizes:**
- Single stage: ~500MB
- Multi-stage: ~200MB (60% reduction!)

---

### Q: Why do you use a non-root user in Docker?
**Answer:**
```dockerfile
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
```

**Security reasons:**
1. **Container escape**: If attacker escapes container, they're not root on host
2. **Principle of least privilege**: App doesn't need root
3. **Compliance**: Many security standards require non-root

**What root can do (that we don't need):**
- Install software
- Modify system files
- Access other processes

---

### Q: What is the HEALTHCHECK in Docker?
**Answer:**
```dockerfile
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/health || exit 1
```

**What it does:**
- Docker periodically checks if container is healthy
- Calls our /health endpoint every 30 seconds
- If it fails 3 times, container is marked "unhealthy"

**Why it matters:**
- Orchestrators (Kubernetes) use this to restart unhealthy containers
- Load balancers can remove unhealthy instances
- Automatic recovery from application failures

---

## 5. GITHUB ACTIONS DEEP DIVE

### Q: What is a GitHub Actions workflow?
**Answer:**
A workflow is an **automated process** defined in YAML that runs in response to events.

**Components:**
- **Workflow**: The entire automation (.yml file)
- **Job**: A set of steps that run on the same runner
- **Step**: Individual task (run command or use action)
- **Action**: Reusable unit (like `actions/checkout`)
- **Runner**: Virtual machine that executes jobs

---

### Q: What are GitHub Secrets? Why use them?
**Answer:**
GitHub Secrets are **encrypted environment variables**.

**We use them for:**
```yaml
${{ secrets.DOCKERHUB_USERNAME }}
${{ secrets.DOCKERHUB_TOKEN }}
```

**Why secrets (not hardcoded):**
1. **Security**: Credentials aren't in code
2. **Audit**: Access is logged
3. **Rotation**: Easy to update without code changes
4. **Visibility**: Masked in logs

**NEVER do this:**
```yaml
password: "mypassword123"  # ❌ WRONG - anyone can see!
```

---

### Q: What is `needs` in GitHub Actions?
**Answer:**
`needs` defines **dependencies between jobs**.

```yaml
docker-build:
  needs: [build-and-test, security-sast, security-sca]
```

This means:
- `docker-build` **waits** for all three jobs to complete
- If ANY of them fails, `docker-build` doesn't run

**Why important:**
- Ensures proper order
- Prevents wasting resources on failed builds
- Creates quality gates

---

### Q: What is an artifact in GitHub Actions?
**Answer:**
An artifact is a **file produced by a job** that can be shared with other jobs.

```yaml
- uses: actions/upload-artifact@v4
  with:
    name: app-jar
    path: target/*.jar
```

**In our pipeline:**
- Build job creates JAR → uploads as artifact
- Docker job downloads artifact → uses it

**Why needed:**
- Jobs run on **different runners** (VMs)
- Files don't persist between jobs
- Artifacts bridge this gap

---

## 6. TROUBLESHOOTING SCENARIOS

### Q: Build passes locally but fails in CI. What would you check?
**Answer:**
1. **Java version mismatch**
   - Local: Java 11, CI: Java 17
   - Fix: Ensure same version

2. **Environment variables**
   - Local has env vars that CI doesn't
   - Fix: Add to GitHub Secrets

3. **Cached files**
   - Local has cached dependencies
   - Fix: Run `mvn clean` locally

4. **OS differences**
   - Local: Windows, CI: Linux
   - Fix: Check path separators, line endings

---

### Q: Docker push fails. How would you debug?
**Answer:**
1. **Check secrets are set:**
   - Go to Settings → Secrets → Actions
   - Verify DOCKERHUB_USERNAME and DOCKERHUB_TOKEN exist

2. **Verify Docker Hub token:**
   - Token might be expired
   - Generate new token at hub.docker.com

3. **Check image name:**
   - Must be lowercase
   - Format: username/image-name

4. **Check workflow logs:**
   - Look at the specific error message
   - Often tells you exactly what's wrong

---

### Q: CodeQL is taking too long. What can you do?
**Answer:**
1. **Reduce scope:**
   - Only scan changed files
   - Exclude test directories

2. **Optimize queries:**
   - Use `security-extended` instead of `security-and-quality`

3. **Increase runner:**
   - Use larger runner (more CPU/RAM)

4. **Cache:**
   - CodeQL supports caching between runs

---

## 7. ADVANCED/TRICKY QUESTIONS

### Q: What would you add to make this production-ready?
**Answer:**
1. **Multiple environments:**
   - Dev → Staging → Production pipeline
   - Different configs per environment

2. **Approval gates:**
   - Manual approval before production deploy

3. **Rollback mechanism:**
   - Keep previous versions
   - Auto-rollback on health check failure

4. **DAST (Dynamic testing):**
   - Test running application for vulnerabilities

5. **Performance testing:**
   - Load testing with JMeter/k6

6. **Better secrets management:**
   - HashiCorp Vault integration

---

### Q: Your dependency check found a CRITICAL CVE. What do you do?
**Answer:**
1. **Assess the vulnerability:**
   - Is the vulnerable code path actually used?
   - What's the attack vector?

2. **Update the dependency:**
   - Check if newer version fixes it
   - Update pom.xml

3. **If no fix available:**
   - Add to suppression file with justification
   - Document the risk and mitigation

4. **If actively exploited:**
   - Treat as emergency
   - Consider alternative library

---

### Q: How would you implement blue-green deployment?
**Answer:**
**Blue-Green deployment** runs two identical environments:
- **Blue**: Current production
- **Green**: New version

**Process:**
1. Deploy new version to Green
2. Run smoke tests on Green
3. Switch load balancer to Green
4. Blue becomes standby (instant rollback if needed)
5. Next release: Green is current, Blue gets new version

**Benefits:**
- Zero downtime
- Instant rollback
- Test in production-like environment

---

### Q: What is the difference between `workflow_dispatch` and `repository_dispatch`?
**Answer:**
| workflow_dispatch | repository_dispatch |
|-------------------|---------------------|
| Triggered from GitHub UI | Triggered via API call |
| Manual button click | External systems can trigger |
| Can have input parameters | Sends custom payload |
| Internal use | Integration with other tools |

**Example use of repository_dispatch:**
- Jenkins finishes a job → triggers GitHub workflow
- Slack command → triggers deployment

---

## 🎓 FINAL TIPS FOR VIVA

1. **Know WHY, not just WHAT**
   - Don't just say "we use CodeQL"
   - Say "we use CodeQL for SAST to catch vulnerabilities like SQL injection BEFORE deployment"

2. **Be ready to trace the flow**
   - "Walk me through what happens when I push code"
   - Know every stage and its purpose

3. **Relate to real-world scenarios**
   - Mention Log4Shell when discussing SCA
   - Mention Equifax breach when discussing why security matters

4. **Admit what you don't know**
   - "I'm not sure, but I would research..." is better than guessing

5. **Show understanding of trade-offs**
   - "We could add more security scans, but it would slow the pipeline"
   - "We prioritized security over speed in our design"

---

## ✅ SELF-CHECK QUESTIONS

Before your viva, make sure you can answer:

- [ ] What is CI/CD and why does it matter?
- [ ] What is shift-left security?
- [ ] What does each stage in your pipeline do?
- [ ] Why is THAT order important?
- [ ] What is SAST vs SCA?
- [ ] What is a CVE?
- [ ] Why use multi-stage Docker builds?
- [ ] Why non-root user in Docker?
- [ ] What are GitHub Secrets and why use them?
- [ ] How would you debug a failing pipeline?

**Good luck! You've got this! 🚀**
