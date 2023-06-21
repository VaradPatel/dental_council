package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IForeignQualificationDetailRepository extends JpaRepository<ForeignQualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM foreign_qualification_details where user_id = :userId and is_verified IN(" + NMRConstants.QUALIFICATION_STATUS_PENDING + "," + NMRConstants.QUALIFICATION_STATUS_APPROVED + ")", nativeQuery = true)
    List<ForeignQualificationDetails> getQualificationDetailsByUserId(BigInteger userId);

    ForeignQualificationDetails findByRequestId(String requestId);
}
