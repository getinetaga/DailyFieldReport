# CI/CD and DevOps Documentation

## Daily Field Report - CI/CD Pipeline & DevOps Guide

### Table of Contents
1. [Overview](#overview)
2. [Pipeline Architecture](#pipeline-architecture)
3. [Setup Instructions](#setup-instructions)
4. [Deployment Process](#deployment-process)
5. [Monitoring & Logging](#monitoring--logging)
6. [Security](#security)
7. [Troubleshooting](#troubleshooting)
8. [Maintenance](#maintenance)

## Overview

This document describes the complete CI/CD pipeline and DevOps setup for the Daily Field Report application. The pipeline includes automated testing, security scanning, code quality checks, containerization, and deployment to multiple environments.

### Key Features
- ✅ Automated CI/CD with GitHub Actions
- ✅ Multi-stage Docker builds
- ✅ Comprehensive testing (unit, integration, security)
- ✅ Code quality analysis with SonarQube
- ✅ Security scanning with OWASP and Snyk
- ✅ Infrastructure as Code
- ✅ Monitoring with Prometheus & Grafana
- ✅ Centralized logging
- ✅ Automated deployments to staging/production

## Pipeline Architecture

### GitHub Actions Workflow
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Code Push     │───▶│   Build & Test  │───▶│ Security Scan   │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│    Deploy       │◀───│   Docker Build  │◀───│ Code Quality    │
│                 │    │                 │    │                 │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Environment Flow
```
Feature Branch ──▶ Pull Request ──▶ Develop ──▶ Staging ──▶ Main ──▶ Production
     │                   │              │          │          │         │
     └─ Unit Tests       └─ Reviews     └─ E2E     └─ UAT     └─ Tag   └─ Release
```

## Setup Instructions

### Prerequisites
- GitHub repository with admin access
- Docker Hub account (or GitHub Container Registry)
- SonarCloud account
- Slack workspace (optional, for notifications)

### 1. Repository Secrets Configuration

Add the following secrets to your GitHub repository:

#### Required Secrets
```bash
# Docker Registry
DOCKER_USERNAME=your_docker_username
DOCKER_PASSWORD=your_docker_password

# SonarQube
SONAR_TOKEN=your_sonarcloud_token

# Security Scanning
SNYK_TOKEN=your_snyk_token

# Deployment (Optional)
HEROKU_API_KEY=your_heroku_api_key
CODACY_PROJECT_TOKEN=your_codacy_token

# Notifications (Optional)
SLACK_WEBHOOK_URL=your_slack_webhook_url
```

#### Database Secrets (for production)
```bash
DB_USER=your_db_username
DB_PASSWORD=your_secure_db_password
REDIS_PASSWORD=your_redis_password
```

### 2. SonarCloud Setup

1. **Connect Repository to SonarCloud:**
   ```bash
   # Login to SonarCloud.io
   # Import your GitHub repository
   # Configure project key: daily-field-report
   # Set organization: getinetaga
   ```

2. **Generate SonarCloud Token:**
   - Go to SonarCloud → My Account → Security
   - Generate new token
   - Add to GitHub secrets as `SONAR_TOKEN`

### 3. Docker Hub Setup

1. **Create Repository:**
   ```bash
   # Create repository: your_username/daily-field-report
   # Set visibility: Private/Public as needed
   ```

2. **Generate Access Token:**
   - Docker Hub → Account Settings → Security
   - Create new access token
   - Add to GitHub secrets

### 4. Local Development Setup

```bash
# Clone repository
git clone https://github.com/getinetaga/DailyFieldReport.git
cd DailyFieldReport

# Build application
mvn clean package

# Run with Docker Compose
docker-compose up -d

# Check application
curl http://localhost:8080/actuator/health
```

## Deployment Process

### Automatic Deployments

#### Development Flow
```bash
# Feature development
git checkout -b feature/new-feature
# Make changes
git add .
git commit -m "Add new feature"
git push origin feature/new-feature

# Create Pull Request → Triggers CI checks
# Merge to develop → Triggers staging deployment
# Merge to main → Triggers production deployment
```

#### Manual Deployment Commands

**Staging Deployment:**
```bash
# Deploy to staging
docker-compose -f docker-compose.yml up -d

# Health check
curl http://staging.daily-field-report.com/actuator/health
```

**Production Deployment:**
```bash
# Deploy to production
docker-compose -f docker-compose.prod.yml up -d

# Health check
curl http://daily-field-report.com/actuator/health
```

### Environment Variables

#### Staging Environment
```bash
SPRING_PROFILES_ACTIVE=staging
DATABASE_URL=jdbc:postgresql://staging-db:5432/fieldreport_staging
REDIS_URL=redis://staging-redis:6379
LOGGING_LEVEL_ROOT=INFO
```

#### Production Environment
```bash
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=jdbc:postgresql://prod-db:5432/fieldreport_prod
REDIS_URL=redis://prod-redis:6379
LOGGING_LEVEL_ROOT=WARN
JAVA_OPTS=-Xmx2g -Xms1g -XX:+UseG1GC
```

## Monitoring & Logging

### Prometheus Metrics

**Application Metrics:**
- HTTP request duration and count
- JVM memory usage
- CPU utilization
- Database connection pool status
- Custom business metrics

**Access Prometheus:**
```bash
# Local development
http://localhost:9090

# Production
http://monitoring.daily-field-report.com:9090
```

### Grafana Dashboards

**Available Dashboards:**
1. **Application Overview** - Key metrics and health status
2. **JVM Metrics** - Memory, GC, and thread monitoring
3. **Database Performance** - Connection pools and query performance
4. **Infrastructure** - System resources and Docker containers

**Access Grafana:**
```bash
# Local development
http://localhost:3000
# Default credentials: admin/admin123

# Production
http://grafana.daily-field-report.com
```

### Log Aggregation

**Log Locations:**
```bash
# Application logs
/app/logs/daily-field-report.log
/app/logs/daily-field-report-error.log
/app/logs/daily-field-report-json.log

# Nginx logs
/var/log/nginx/access.log
/var/log/nginx/error.log
```

**Log Analysis Commands:**
```bash
# View application logs
docker-compose logs -f daily-field-report

# View error logs only
docker-compose logs -f daily-field-report | grep ERROR

# Monitor access logs
docker-compose logs -f nginx | grep access
```

## Security

### Security Scanning

**OWASP Dependency Check:**
```bash
# Run dependency check
mvn org.owasp:dependency-check-maven:check

# View report
open target/dependency-check-report.html
```

**Snyk Vulnerability Scanning:**
```bash
# Install Snyk CLI
npm install -g snyk

# Authenticate
snyk auth

# Run security scan
snyk test

# Monitor for new vulnerabilities
snyk monitor
```

### Security Best Practices

1. **Container Security:**
   - Non-root user in Docker container
   - Minimal base image (Alpine Linux)
   - Regular security updates

2. **Network Security:**
   - Nginx reverse proxy with security headers
   - Rate limiting
   - HTTPS/TLS encryption

3. **Application Security:**
   - Input validation
   - SQL injection protection
   - XSS protection headers

### Security Headers Configuration

```nginx
# In nginx.conf
add_header X-Frame-Options "SAMEORIGIN" always;
add_header X-XSS-Protection "1; mode=block" always;
add_header X-Content-Type-Options "nosniff" always;
add_header Referrer-Policy "no-referrer-when-downgrade" always;
add_header Strict-Transport-Security "max-age=63072000" always;
```

## Troubleshooting

### Common Issues

#### Build Failures

**Maven Build Issues:**
```bash
# Clean and rebuild
mvn clean install -X

# Skip tests for quick build
mvn clean package -DskipTests

# Check Java version
java -version
mvn -version
```

**Docker Build Issues:**
```bash
# Clean Docker cache
docker system prune -a

# Build with no cache
docker build --no-cache -t daily-field-report .

# Check Docker logs
docker logs daily-field-report-app
```

#### Deployment Issues

**Application Won't Start:**
```bash
# Check logs
docker-compose logs daily-field-report

# Check environment variables
docker-compose exec daily-field-report env

# Verify health endpoint
curl http://localhost:8080/actuator/health
```

**Database Connection Issues:**
```bash
# Check database status
docker-compose ps postgres

# Test database connection
docker-compose exec postgres psql -U fieldreport -d fieldreport

# Check connection string
docker-compose exec daily-field-report env | grep DATABASE
```

#### Monitoring Issues

**Prometheus Not Scraping:**
```bash
# Check Prometheus targets
curl http://localhost:9090/api/v1/targets

# Verify application metrics endpoint
curl http://localhost:8080/actuator/prometheus

# Check Prometheus configuration
docker-compose exec prometheus cat /etc/prometheus/prometheus.yml
```

**Grafana Dashboard Issues:**
```bash
# Check Grafana logs
docker-compose logs grafana

# Verify datasource connection
curl http://localhost:3000/api/datasources

# Reset admin password
docker-compose exec grafana grafana-cli admin reset-admin-password admin123
```

### Performance Troubleshooting

**High Memory Usage:**
```bash
# Check JVM memory settings
docker-compose exec daily-field-report java -XX:+PrintFlagsFinal -version | grep Heap

# Monitor memory usage
docker stats daily-field-report-app

# Generate heap dump
docker-compose exec daily-field-report jcmd 1 GC.run_finalization
```

**Slow Response Times:**
```bash
# Check application metrics
curl http://localhost:8080/actuator/metrics/http.server.requests

# Monitor database queries
docker-compose logs postgres | grep "duration:"

# Check CPU usage
docker stats --format "table {{.Container}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

## Maintenance

### Regular Maintenance Tasks

#### Weekly Tasks
```bash
# Update dependencies
mvn versions:display-dependency-updates

# Run security scans
mvn org.owasp:dependency-check-maven:check
snyk test

# Clean up Docker images
docker system prune -f
```

#### Monthly Tasks
```bash
# Rotate logs
find logs/ -name "*.log" -type f -mtime +30 -delete

# Update base images
docker pull eclipse-temurin:23-jre-alpine
docker pull postgres:15-alpine
docker pull nginx:alpine

# Backup database
docker-compose exec postgres pg_dump -U fieldreport fieldreport > backup.sql
```

### Version Updates

**Application Updates:**
```bash
# Update version in pom.xml
mvn versions:set -DnewVersion=1.1.0

# Commit and tag
git add .
git commit -m "Bump version to 1.1.0"
git tag v1.1.0
git push origin main --tags
```

**Dependency Updates:**
```bash
# Check for updates
mvn versions:display-dependency-updates

# Update dependencies
mvn versions:use-latest-versions

# Test and commit
mvn test
git add .
git commit -m "Update dependencies"
```

### Backup and Recovery

**Database Backup:**
```bash
# Create backup
docker-compose exec postgres pg_dump -U fieldreport fieldreport > backup_$(date +%Y%m%d).sql

# Restore backup
docker-compose exec -T postgres psql -U fieldreport fieldreport < backup_20231025.sql
```

**Application Data Backup:**
```bash
# Backup exports directory
tar -czf exports_backup_$(date +%Y%m%d).tar.gz exports/

# Backup logs
tar -czf logs_backup_$(date +%Y%m%d).tar.gz logs/
```

### Scaling and Performance

**Horizontal Scaling:**
```bash
# Scale application instances
docker-compose up -d --scale daily-field-report=3

# Add load balancer configuration
# Update nginx upstream configuration
```

**Vertical Scaling:**
```bash
# Update resource limits in docker-compose.yml
deploy:
  resources:
    limits:
      cpus: '4.0'
      memory: 4G
    reservations:
      cpus: '2.0'
      memory: 2G
```

## Contact and Support

For issues and questions:
- **Technical Issues:** Create GitHub issue
- **Security Concerns:** Email security@fieldreport.com
- **General Support:** support@fieldreport.com

## Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Documentation](https://docs.docker.com)
- [SonarQube Documentation](https://docs.sonarqube.org)
- [Prometheus Documentation](https://prometheus.io/docs)
- [Grafana Documentation](https://grafana.com/docs)