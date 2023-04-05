package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.HpProfile;
import in.gov.abdm.nmr.jpa.entity.RegistrationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IRegistrationDetailRepository extends JpaRepository<RegistrationDetails, Long> {

    @Query(value = "SELECT * FROM registration_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetails getRegistrationDetailsByHpProfileId(BigInteger hpProfileId);

    @Query(value = "SELECT registration_no FROM main.registration_details WHERE hp_profile_id = :hpProfileId", nativeQuery = true)
    List<String> getRegistrationNosByHpProfileId(BigInteger hpProfileId);

    @Query(value = "SELECT * FROM hp_profile where registration_id =:registrationId ORDER BY id asc LIMIT 2 OFFSET 1", nativeQuery = true)
    HpProfile findSecondLastHpProfile(BigInteger registrationId);

    @Query(value = "SELECT hp_profile_id FROM registration_details where registration_no =:registrationNo", nativeQuery = true)
    List<BigInteger> fetchHpProfileIdByRegistrationNumber(String registrationNo);

    @Query(value = "SELECT * FROM registration_details where registration_no =:registrationNo and state_medical_council_id =:stateMedicalCouncilId", nativeQuery = true)
    RegistrationDetails fetchHpProfileIdByRegistrationNumberAndStateMedicalCouncilId(String registrationNo, BigInteger stateMedicalCouncilId);
}
