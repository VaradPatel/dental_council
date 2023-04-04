package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.HpProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;

public interface IHpProfileMasterRepository extends JpaRepository<HpProfileMaster, BigInteger> {

    @Query(value = "SELECT * FROM hp_profile_master hp where registration_id=:registrationId", nativeQuery = true)
    HpProfileMaster findByRegistrationId(String registrationId);

    HpProfileMaster findHpProfileMasterById(BigInteger hpProfileId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hp_profile_master SET email_id =:email  WHERE id =:masterHpProfileId", nativeQuery = true)
    void updateMasterHpProfileEmail(BigInteger masterHpProfileId, String email);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hp_profile_master SET mobile_Number =:mobileNumber  WHERE id =:masterHpProfileId", nativeQuery = true)
    void updateMasterHpProfileMobile(BigInteger masterHpProfileId, String mobileNumber);
}
