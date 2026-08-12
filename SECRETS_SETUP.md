# GitHub Actions Secrets Setup Guide

This document provides step-by-step instructions for setting up all required secrets for the Daily Field Report CI/CD pipeline.

## Required Secrets

### Docker Registry Secrets

1. **DOCKER_USERNAME**
   - Go to Docker Hub → Account Settings
   - Your username is displayed at the top
   - Add this to GitHub Secrets

2. **DOCKER_PASSWORD**
   - Go to Docker Hub → Account Settings → Security
   - Click "New Access Token"
   - Name: "GitHub Actions"
   - Permissions: Read, Write, Delete
   - Copy the generated token
   - Add to GitHub Secrets

### SonarCloud Secrets

1. **SONAR_TOKEN**
   - Go to [SonarCloud.io](https://sonarcloud.io)
   - Sign in with GitHub
   - My Account → Security
   - Generate Token → Name: "GitHub Actions"
   - Copy the token
   - Add to GitHub Secrets

### Security Scanning Secrets

1. **SNYK_TOKEN**
   - Go to [Snyk.io](https://snyk.io)
   - Sign up/Login
   - Account Settings → General → API Token
   - Copy the token
   - Add to GitHub Secrets

### Code Quality Secrets

1. **CODACY_PROJECT_TOKEN** (Optional)
   - Go to [Codacy.com](https://www.codacy.com)
   - Add your repository
   - Project Settings → Integrations → Add Coverage
   - Copy the project token
   - Add to GitHub Secrets

### Notification Secrets

1. **SLACK_WEBHOOK_URL** (Optional)
   - Go to your Slack workspace
   - Apps → Incoming Webhooks
   - Add to Slack → Choose channel
   - Copy the Webhook URL
   - Add to GitHub Secrets

### Production Database Secrets

1. **DB_USER**
   - Your production database username
   - Example: `fieldreport_prod`

2. **DB_PASSWORD**
   - Your production database password
   - Use a strong, unique password
   - Example: Generate with `openssl rand -base64 32`

3. **REDIS_PASSWORD**
   - Your Redis instance password
   - Example: Generate with `openssl rand -base64 16`

## How to Add Secrets to GitHub

1. **Navigate to Repository:**
   - Go to your GitHub repository
   - Click "Settings" tab

2. **Access Secrets:**
   - In left sidebar, click "Secrets and variables"
   - Click "Actions"

3. **Add New Secret:**
   - Click "New repository secret"
   - Enter Name (exactly as shown above)
   - Enter Value
   - Click "Add secret"

## Environment-Specific Secrets

### Staging Environment
```
STAGING_DB_USER=fieldreport_staging
STAGING_DB_PASSWORD=<staging-password>
STAGING_REDIS_PASSWORD=<staging-redis-password>
```

### Production Environment
```
PROD_DB_USER=fieldreport_prod
PROD_DB_PASSWORD=<production-password>
PROD_REDIS_PASSWORD=<production-redis-password>
```

## Verification

After adding all secrets, verify the setup:

1. **Check Secret Names:**
   - Ensure exact naming (case-sensitive)
   - No extra spaces or characters

2. **Test Pipeline:**
   - Push a commit to trigger the workflow
   - Check Actions tab for any secret-related errors

3. **Common Issues:**
   - Invalid Docker credentials
   - Expired SonarCloud token
   - Incorrect Slack webhook URL

## Security Best Practices

1. **Token Rotation:**
   - Rotate tokens every 90 days
   - Update secrets when team members leave

2. **Principle of Least Privilege:**
   - Only grant necessary permissions
   - Use read-only tokens where possible

3. **Regular Audits:**
   - Review secret usage monthly
   - Remove unused secrets

## Support

If you encounter issues:
1. Check GitHub Actions logs for specific error messages
2. Verify token permissions on respective platforms
3. Ensure secrets are not accidentally logged or exposed