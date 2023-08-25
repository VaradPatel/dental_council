package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.RegistrationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigInteger;
import java.util.List;

public interface IRegistrationDetailRepository extends JpaRepository<RegistrationDetails, Long> {

    @Query(value = "SELECT * FROM registration_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetails getRegistrationDetailsByHpProfileId(BigInteger hpProfileId);

    @Query(value = "SELECT hp_profile_id FROM registration_details where registration_no =:registrationNo", nativeQuery = true)
    List<BigInteger> fetchHpProfileIdByRegistrationNumber(String registrationNo);

    @Query(value = "select * from registration_details rd where rd.registration_no =:registrationNo and rd.state_medical_council_id =:stateMedicalCouncilId order by updated_at desc limit 1  ",nativeQuery = true)
    RegistrationDetails getRegistrationDetailsByRegistrationNoAndStateMedicalCouncilId(String registrationNo,BigInteger stateMedicalCouncilId);

    @Query(value = "select * from main.registration_details rd JOIN main.state_medical_council smc ON rd.state_medical_council_id = smc.id and smc.user_id = :userId and rd.hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetails isHPBelongsToLoginSMC(BigInteger userId, BigInteger hpProfileId);

}
