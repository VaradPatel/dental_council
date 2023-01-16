package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.entity.WorkProfileAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkProfileAuditRepository extends JpaRepository<WorkProfileAudit, BigInteger> {


}
