package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.SecurityAuditTrail;

public interface ISecurityAuditTrailRepository extends JpaRepository<SecurityAuditTrail, BigInteger> {

    List<SecurityAuditTrail> findByUsernameAndCreatedAtGreaterThanEqualAndStatus(String user, Timestamp after, String status);
}
