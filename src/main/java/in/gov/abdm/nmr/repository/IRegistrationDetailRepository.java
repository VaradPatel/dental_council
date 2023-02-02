package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IRegistrationDetailRepository extends JpaRepository<RegistrationDetails, Long> {

    @Query(value = "SELECT * FROM registration_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetails getRegistrationDetailsByHpProfileId(BigInteger hpProfileId);

    @Query(value = "SELECT * FROM hp_profile where registration_id =:registrationId ORDER BY id asc LIMIT 2 OFFSET 1", nativeQuery = true)
    HpProfile findSecondLastHpProfile(BigInteger registrationId);


}
