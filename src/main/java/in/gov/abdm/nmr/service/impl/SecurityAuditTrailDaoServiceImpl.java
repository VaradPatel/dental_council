package in.gov.abdm.nmr.service.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.repository.ISecurityAuditTrailRepository;
import in.gov.abdm.nmr.service.ISecurityAuditTrailDaoService;

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

}
