package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface IHpProfileMasterRepository extends JpaRepository<HpProfileMaster, BigInteger> {

    @Query(value = "SELECT hp FROM hp_profile_master hp where registration_id=:registrationId", nativeQuery = true)
    HpProfileMaster findByRegistrationId(String registrationId);

    HpProfileMaster findHpProfileMasterById(BigInteger hpProfileId);

}
