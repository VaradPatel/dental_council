package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.SecurityAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public interface ISecurityAuditTrailRepository extends JpaRepository<SecurityAuditTrail, BigInteger> {

    List<SecurityAuditTrail> findByUsernameAndCreatedAtGreaterThanEqualAndStatus(String user, Timestamp after, String status);
    
    SecurityAuditTrail findByCorrelationId(String correlationId);
}
