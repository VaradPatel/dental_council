package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileAudit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHpProfileAuditRepository extends JpaRepository<HpProfileAudit, BigInteger> {

    @Query(value = "SELECT hp FROM main.hp_profile_audit hp where registration_id=:registrationId")
    HpProfileAudit findByRegistrationId(BigInteger registrationId);
}
