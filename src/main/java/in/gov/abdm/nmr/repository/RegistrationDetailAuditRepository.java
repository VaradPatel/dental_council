package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetailsAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationDetailAuditRepository extends JpaRepository<RegistrationDetailsAudit, Long> {


}