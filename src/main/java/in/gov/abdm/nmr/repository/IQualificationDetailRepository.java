package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public interface IQualificationDetailRepository extends JpaRepository<QualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details where user_id = :userId and is_verified IN(" + NMRConstants.QUALIFICATION_STATUS_PENDING + "," + NMRConstants.QUALIFICATION_STATUS_APPROVED + ")", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByUserId(BigInteger userId);

    
    @Query(value = "SELECT qd.name as qualificationName, qd.qualification_year as qualificationYear, u.name as universityName FROM qualification_details qd " + //
            "left join universities u on qd.university_id = u.id where qd.hp_profile_id =:hpprofileId", nativeQuery = true)
    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);

    QualificationDetails findByRequestId(String requestId);
}
