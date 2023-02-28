package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface RegistrationDetailMasterRepository extends JpaRepository<RegistrationDetailsMaster, Long> {

    @Query(value = "SELECT * FROM registration_details_master where hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetailsMaster getRegistrationDetailsByHpProfileId(BigInteger hpProfileId);

}
