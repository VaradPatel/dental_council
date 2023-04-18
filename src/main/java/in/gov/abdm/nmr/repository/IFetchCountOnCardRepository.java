package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import in.gov.abdm.nmr.mapper.IStatusCount;
import in.gov.abdm.nmr.mapper.IStatusWiseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
public interface IFetchCountOnCardRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_COUNT_QUERY_FOR_SMC, nativeQuery = true)
    List<IStatusCount> fetchCountForSmc(@Param(SMC_PROFILE_ID) BigInteger smcProfileId);

    @Query(value = FETCH_COUNT_QUERY_FOR_COLLEGE, nativeQuery = true)
    List<IStatusCount> fetchCountForCollege(@Param(COLLEGE_ID) BigInteger collegeId);

    @Query(value = FETCH_COUNT_QUERY_FOR_NMC, nativeQuery = true)
    List<IStatusCount> fetchCountForNmc();

    @Query(value = FETCH_COUNT_QUERY_FOR_NBE, nativeQuery = true)
    List<IStatusCount> fetchCountForNbe();
}
