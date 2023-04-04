package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.jpa.repository.ISecurityAuditTrailRepository;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class SecurityAuditTrailDaoServiceImpl implements ISecurityAuditTrailDaoService {
    
    private ISecurityAuditTrailRepository securityAuditTrailRepository;
    
    public SecurityAuditTrailDaoServiceImpl(ISecurityAuditTrailRepository securityAuditTrailRepository) {
        this.securityAuditTrailRepository = securityAuditTrailRepository;
    }
    
    @Override
    public SecurityAuditTrail saveAndFlush(SecurityAuditTrail securityAuditTrail) {
        return securityAuditTrailRepository.saveAndFlush(securityAuditTrail);
    }

    @Override
    public List<SecurityAuditTrail> findByUserAndCreatedAtGreaterThanEqualAndStatus(String user, Timestamp after, String status) {
        return securityAuditTrailRepository.findByUsernameAndCreatedAtGreaterThanEqualAndStatus(user, after, status);
    }

    @Override
    public SecurityAuditTrail findByCorrelationId(String correlationId) {
        return securityAuditTrailRepository.findByCorrelationId(correlationId);
    }

}
