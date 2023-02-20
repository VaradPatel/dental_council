package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IHpProfileMasterRepository extends JpaRepository<HpProfileMaster, BigInteger> {

    @Query(value = "SELECT hp FROM hp_profile_master hp where registration_id=:registrationId", nativeQuery = true)
    HpProfileMaster findByRegistrationId(String registrationId);

    HpProfileMaster findHpProfileMasterById(BigInteger hpProfileId);

}
