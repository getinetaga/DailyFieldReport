# Deployment Procedures

## Daily Field Report - Deployment Guide

### Quick Deployment Commands

#### Local Development
```bash
# Start all services
docker-compose up -d

# Stop all services
docker-compose down

# View logs
docker-compose logs -f daily-field-report
```

#### Staging Deployment
```bash
# Deploy to staging
git push origin develop

# Manual staging deployment
docker-compose -f docker-compose.yml up -d

# Health check
curl https://staging.daily-field-report.com/actuator/health
```

#### Production Deployment
```bash
# Create release
git tag v1.0.1
git push origin v1.0.1

# Manual production deployment
docker-compose -f docker-compose.prod.yml up -d

# Health check
curl https://daily-field-report.com/actuator/health
```

### Deployment Checklist

#### Pre-Deployment
- [ ] All tests passing
- [ ] Code reviewed and approved
- [ ] Security scan completed
- [ ] Database migrations tested
- [ ] Backup current production data
- [ ] Notify team of deployment window

#### During Deployment
- [ ] Deploy to staging first
- [ ] Run smoke tests on staging
- [ ] Deploy to production
- [ ] Monitor application health
- [ ] Check key functionality
- [ ] Monitor error logs

#### Post-Deployment
- [ ] Verify all features working
- [ ] Check performance metrics
- [ ] Monitor for 24 hours
- [ ] Update documentation
- [ ] Notify stakeholders

### Rollback Procedures

#### Automatic Rollback
```bash
# Rollback to previous version
docker-compose pull daily-field-report:previous
docker-compose up -d daily-field-report

# Or rollback using Git
git revert <commit-hash>
git push origin main
```

#### Database Rollback
```bash
# Restore from backup
docker-compose exec postgres psql -U fieldreport fieldreport < backup_before_deployment.sql
```

### Environment Configuration

#### Environment Variables by Stage

**Development:**
```env
SPRING_PROFILES_ACTIVE=development
LOG_LEVEL=DEBUG
DATABASE_URL=jdbc:h2:mem:testdb
```

**Staging:**
```env
SPRING_PROFILES_ACTIVE=staging
LOG_LEVEL=INFO
DATABASE_URL=jdbc:postgresql://staging-db:5432/fieldreport_staging
```

**Production:**
```env
SPRING_PROFILES_ACTIVE=production
LOG_LEVEL=WARN
DATABASE_URL=jdbc:postgresql://prod-db:5432/fieldreport_prod
```