package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

public interface IForeignQualificationDetailRepository extends JpaRepository<ForeignQualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM foreign_qualification_details where user_id = :userId and is_verified !=" + NMRConstants.QUALIFICATION_STATUS_REJECTED + "", nativeQuery = true)
    List<ForeignQualificationDetails> getQualificationDetailsByUserId(BigInteger userId);

    @Query(value = "SELECT * FROM foreign_qualification_details where user_id = :userId order by created_at asc limit 1", nativeQuery = true)
    ForeignQualificationDetails getBasicQualificationDetailsByUserId(BigInteger userId);

    @Query(value = "SELECT * FROM foreign_qualification_details where user_id = :userId and is_verified =" + NMRConstants.QUALIFICATION_STATUS_APPROVED + "", nativeQuery = true)
    List<ForeignQualificationDetails> getApprovedQualificationDetailsByUserId(BigInteger userId);

    ForeignQualificationDetails findByRequestId(String requestId);

    @Query(value = "SELECT * FROM foreign_qualification_details where user_id = :userId and is_verified =" + NMRConstants.QUALIFICATION_STATUS_PENDING + "", nativeQuery = true)
    List<ForeignQualificationDetails> getPendingQualificationsByUserId(BigInteger userId);

    @Modifying
    @Transactional
    @Query(value = "delete from foreign_qualification_details where user_id = :userId ",nativeQuery = true)    void deleteInternationalQualificationByUserId(BigInteger userId);
}
