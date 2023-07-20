package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.List;

public interface IQualificationDetailRepository extends JpaRepository<QualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details where user_id = :userId and is_verified !=" + NMRConstants.QUALIFICATION_STATUS_REJECTED + "", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByUserId(BigInteger userId);

    @Query(value = "SELECT * FROM qualification_details where user_id = :userId and is_verified =" + NMRConstants.QUALIFICATION_STATUS_APPROVED + "", nativeQuery = true)
    List<QualificationDetails> getApprovedQualificationDetailsByUserId(BigInteger userId);

    @Query(value = "SELECT qd.name as qualificationName, qd.qualification_year as qualificationYear, u.name as universityName FROM qualification_details qd " +
            "left join universities u on qd.university_id = u.id where qd.hp_profile_id =:hpprofileId", nativeQuery = true)
    List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId);

    QualificationDetails findByRequestId(String requestId);

    @Query(value = "select c.course_name  from qualification_details qd join course c on qd.course_id =c.id where qd.user_id =:userId and is_verified !=" + NMRConstants.QUALIFICATION_STATUS_REJECTED +
            " union select fqd.course from foreign_qualification_details fqd where fqd.user_id =:userId and is_verified !=" + NMRConstants.QUALIFICATION_STATUS_REJECTED + "", nativeQuery = true)
    List<String> getListOfQualificationByUserID(BigInteger userId);

    @Query(value = "SELECT * FROM qualification_details where user_id = :userId and is_verified =" + NMRConstants.QUALIFICATION_STATUS_PENDING + "", nativeQuery = true)
    List<QualificationDetails> getPendingQualificationsByUserId(BigInteger userId);

    @Modifying
    @Transactional
    @Query(value = "delete from qualification_details where user_id = :userId", nativeQuery = true)
    void deleteIndianQualificationByUserId(BigInteger userId);
}
