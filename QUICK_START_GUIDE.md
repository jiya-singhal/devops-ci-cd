# 🚀 QUICK START GUIDE
## Step-by-Step Setup Instructions

This guide will help you set up everything from scratch. Follow each step carefully.

---

## 📋 PREREQUISITES

Before you begin, you need:

### 1. GitHub Account
- If you don't have one: Go to [github.com](https://github.com) and sign up (free)

### 2. Docker Hub Account
- Go to [hub.docker.com](https://hub.docker.com)
- Click "Sign Up" (free tier is fine)
- **Remember your username** - you'll need it!

### 3. Git Installed on Your Computer
- **Windows**: Download from [git-scm.com](https://git-scm.com/download/win)
- **Mac**: Run `brew install git` or download from git-scm.com
- **Linux**: Run `sudo apt install git` (Ubuntu/Debian)

### 4. (Optional) Java and Maven for Local Testing
- Only needed if you want to run locally
- Download Java 17: [adoptium.net](https://adoptium.net)
- Download Maven: [maven.apache.org](https://maven.apache.org/download.cgi)

---

## 🔧 STEP 1: CREATE GITHUB REPOSITORY

1. Go to [github.com](https://github.com) and log in
2. Click the **+** icon (top right) → **New repository**
3. Fill in:
   - **Repository name**: `devops-cicd-demo`
   - **Description**: "DevOps CI/CD Pipeline with GitHub Actions"
   - **Visibility**: Public (required for free GitHub Actions minutes)
   - **DO NOT** check "Add a README file" (we have our own)
4. Click **Create repository**
5. **Keep this page open** - you'll need the URL!

---

## 🔑 STEP 2: CREATE DOCKER HUB ACCESS TOKEN

This is **CRITICAL** - without this, the pipeline won't be able to push images!

1. Go to [hub.docker.com](https://hub.docker.com) and log in
2. Click your **profile icon** (top right) → **Account Settings**
3. Go to **Security** tab
4. Click **New Access Token**
5. Fill in:
   - **Description**: "GitHub Actions CI/CD"
   - **Access permissions**: Read & Write
6. Click **Generate**
7. **COPY THE TOKEN NOW** - You won't see it again!
8. Save it somewhere safe (temporarily)

---

## 🔒 STEP 3: ADD SECRETS TO GITHUB

1. Go to your new GitHub repository
2. Click **Settings** tab (top right)
3. In the left sidebar, click **Secrets and variables** → **Actions**
4. Click **New repository secret**

### Add First Secret:
- **Name**: `DOCKERHUB_USERNAME`
- **Secret**: Your Docker Hub username (exactly as you use to login)
- Click **Add secret**

### Add Second Secret:
- Click **New repository secret** again
- **Name**: `DOCKERHUB_TOKEN`
- **Secret**: Paste the token you copied in Step 2
- Click **Add secret**

✅ You should now see 2 secrets listed!

---

## 📁 STEP 4: DOWNLOAD AND PREPARE PROJECT FILES

### Option A: Download from This Session
If I provided you with downloadable files, extract them to a folder.

### Option B: Create Files Manually
If you're creating manually, you need these files:
```
devops-cicd-demo/
├── .github/
│   └── workflows/
│       └── ci.yml          ← The pipeline (MOST IMPORTANT)
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java
│   │   │   ├── HelloController.java
│   │   │   └── CalculatorService.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/example/demo/
│           ├── CalculatorServiceTest.java
│           └── HelloControllerTest.java
├── Dockerfile
├── .dockerignore
├── .gitignore
├── pom.xml
├── checkstyle.xml
├── dependency-check-suppression.xml
└── README.md
```

---

## 📤 STEP 5: PUSH CODE TO GITHUB

Open a terminal/command prompt and run these commands:

```bash
# 1. Navigate to your project folder
cd path/to/devops-cicd-demo

# 2. Initialize Git
git init

# 3. Add all files
git add .

# 4. Create first commit
git commit -m "Initial commit: DevOps CI/CD demo project"

# 5. Connect to your GitHub repository
# Replace YOUR_USERNAME with your actual GitHub username
git remote add origin https://github.com/YOUR_USERNAME/devops-cicd-demo.git

# 6. Push to GitHub
git branch -M main
git push -u origin main
```

If prompted for credentials, use:
- Username: Your GitHub username
- Password: Your GitHub personal access token (NOT your password!)

### Creating GitHub Personal Access Token (if needed):
1. Go to GitHub → Settings → Developer settings → Personal access tokens → Tokens (classic)
2. Generate new token with `repo` scope
3. Use this token as your password

---

## 🎬 STEP 6: WATCH YOUR PIPELINE RUN!

1. Go to your GitHub repository
2. Click the **Actions** tab
3. You should see a workflow running!
4. Click on it to see the details
5. Click on individual jobs to see logs

### What You Should See:
```
✅ Build & Test        (should pass)
✅ SAST Security Scan  (should pass)
✅ Dependency Scan     (should pass)
✅ Build Docker Image  (should pass)
✅ Scan Docker Image   (should pass)
✅ Container Test      (should pass)
✅ Push to Docker Hub  (should pass)
```

If everything passes, your image is now on Docker Hub!

---

## 🔍 STEP 7: VERIFY ON DOCKER HUB

1. Go to [hub.docker.com](https://hub.docker.com)
2. Log in and go to **Repositories**
3. You should see `devops-cicd-demo` repository
4. Click on it to see your pushed image

### Pull and Test Your Image:
```bash
# Pull your image
docker pull YOUR_DOCKERHUB_USERNAME/devops-cicd-demo:latest

# Run it
docker run -p 8080:8080 YOUR_DOCKERHUB_USERNAME/devops-cicd-demo:latest

# Test it (in another terminal)
curl http://localhost:8080/health
# Should return: OK

curl http://localhost:8080/hello
# Should return: Hello, World! Welcome to DevOps CI/CD Demo.
```

---

## 📝 STEP 8: CUSTOMIZE FOR YOUR SUBMISSION

### Update README.md:
1. Replace `[YOUR_USERNAME]` with your actual GitHub username
2. Update any other placeholder text

### Update Proposal PDF:
1. Replace `[YOUR NAME HERE]` with your actual name
2. Replace `[YOUR STUDENT ID]` with your Scaler Student ID
3. Update GitHub URL to your actual repository

### Update Report PDF:
1. Replace `[YOUR NAME]` with your actual name
2. Replace `[YOUR SCALER ID]` with your Scaler Student ID
3. Update GitHub URL

### Rename Proposal File:
Rename `Project_Proposal.pdf` to:
`YourName_YourScalerID_DevOps_CI_Proposal.pdf`

---

## 📊 STEP 9: SUBMIT YOUR PROPOSAL

1. Go to the Google Form provided by your instructor
2. Upload your renamed proposal PDF
3. Submit!

---

## ✅ FINAL CHECKLIST

Before final submission (Jan 18, 2026):

- [ ] GitHub repository is public
- [ ] Pipeline runs successfully (all green ✅)
- [ ] Image is pushed to Docker Hub
- [ ] README is updated with your info
- [ ] Proposal PDF has your name and ID
- [ ] Report PDF has your name and ID
- [ ] Studied VIVA_PREPARATION.md thoroughly
- [ ] Can explain WHY each pipeline stage exists
- [ ] Can demo the pipeline running
- [ ] Can explain security concepts (SAST, SCA, CVE)

---

## ❓ TROUBLESHOOTING

### Pipeline Fails at Docker Push:
- Check DOCKERHUB_USERNAME and DOCKERHUB_TOKEN secrets
- Make sure token hasn't expired
- Verify username is correct (case-sensitive)

### Pipeline Fails at Tests:
- Check if all test files are properly created
- Look at the error message in the logs
- Run tests locally: `mvn test`

### CodeQL Takes Forever:
- First run is slow (building database)
- Subsequent runs are faster (cached)
- Just wait - it can take 5-10 minutes

### Git Push Rejected:
- You might have pushed from GitHub web first
- Try: `git pull --rebase origin main` then push again

### "Permission Denied" Errors:
- On Mac/Linux: Try with `sudo`
- On Windows: Run terminal as Administrator

---

## 🎉 YOU'RE DONE!

Congratulations! You now have:
1. ✅ A working CI/CD pipeline
2. ✅ Automated security scanning
3. ✅ Containerized application
4. ✅ Image on Docker Hub
5. ✅ Complete documentation

**Good luck with your viva!** 🚀
