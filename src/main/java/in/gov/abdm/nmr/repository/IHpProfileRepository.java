package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

public interface IHpProfileRepository extends JpaRepository<HpProfile, BigInteger> {

    @Query(value = "SELECT * FROM hp_profile WHERE id=(SELECT MAX(id) FROM hp_profile WHERE user_id=:userId)", nativeQuery = true)
    HpProfile findLatestEntryByUserid(BigInteger userId);

    HpProfile findHpProfileById(BigInteger id);

    @Query(value = "SELECT * FROM hp_profile where registration_id =:registrationId ORDER BY id DESC LIMIT 1", nativeQuery = true)
    HpProfile findLatestHpProfileByRegistrationId(String registrationId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hp_profile SET mobile_Number =:mobileNumber  WHERE id =:hpProfileId", nativeQuery = true)
    void updateHpProfileMobile(BigInteger hpProfileId, String mobileNumber);

    @Query(value = "select hpm.id from hp_profile hp join hp_profile_master hpm on hp.registration_id = hpm.registration_id where hp.id =:hpProfileId", nativeQuery = true)
    BigInteger findMasterHpProfileByHpProfileId(BigInteger hpProfileId);

    @Query(value = "SELECT * FROM hp_profile where transaction_id =:transactionId ", nativeQuery = true)
    HpProfile findByTransactionId(String transactionId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE hp_profile SET e_sign_status =:status  WHERE id =:hpProfileId", nativeQuery = true)
    void updateEsignStatus(BigInteger hpProfileId, String status);

}
