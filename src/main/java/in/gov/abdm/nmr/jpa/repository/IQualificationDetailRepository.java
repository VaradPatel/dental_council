package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.QualificationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public interface IQualificationDetailRepository extends JpaRepository<QualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details where registration_details_id = :registrationId", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByRegistrationId(Integer registrationId);

    @Query(value = "SELECT * FROM qualification_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByHpProfileId(BigInteger hpProfileId);

    
    @Query(value = "SELECT qd.name as qualificationName, qd.qualification_year as qualificationYear, u.name as universityName FROM qualification_details qd " + //
            "left join universities u on qd.university_id = u.id where qd.hp_profile_id =:hpprofileId", nativeQuery = true)
    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);

    List<QualificationDetails> findByRequestId(String requestId);
}
