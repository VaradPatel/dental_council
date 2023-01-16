package in.gov.abdm.nmr.service;

import java.sql.Timestamp;
import java.util.List;

import in.gov.abdm.nmr.entity.SecurityAuditTrail;

public interface ISecurityAuditTrailDaoService {

    SecurityAuditTrail saveAndFlush(SecurityAuditTrail securityAuditTrail);
    
    List<SecurityAuditTrail> findByUserAndCreatedAtGreaterThanEqualAndStatus(String user, Timestamp after, String status);
    
    SecurityAuditTrail findByCorrelationId(String correlationId);
}
