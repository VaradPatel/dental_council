package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.gov.abdm.nmr.entity.QualificationDetails;

public interface IQualificationDetailRepository extends JpaRepository<QualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details where registration_details_id = :registrationId", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByRegistrationId(Integer registrationId);

    @Query(value = "SELECT qd.name as qualificationName, qd.qualification_year as qualificationYear, u.name as universityName FROM qualification_details qd " + //
            "left join universities u on qd.university_id = u.id where qd.hp_profile_id =:hpprofileId", nativeQuery = true)
    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);

    List<QualificationDetails> findByRequestId(String requestId);
}
