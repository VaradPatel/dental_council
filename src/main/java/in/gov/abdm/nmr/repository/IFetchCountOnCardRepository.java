package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
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

    @Query(value = FETCH_STATUS_WISE_COUNT_QUERY,nativeQuery = true)
    List<IStatusWiseCount> fetchStatusWiseCount(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId);

    @Query(value = FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_HP,nativeQuery = true)
    List<IStatusWiseCount> fetchUserSpecificStatusWiseCountForHp(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId, @Param(HP_PROFILE_ID) BigInteger hpProfileId);

    @Query(value = FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_DEAN,nativeQuery = true)
    List<IStatusWiseCount> fetchUserSpecificStatusWiseCountForDean(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId, @Param(COLLEGE_DEAN_ID) BigInteger collegeDeanId);

    @Query(value = FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_REGISTRAR,nativeQuery = true)
    List<IStatusWiseCount> fetchUserSpecificStatusWiseCountForRegistrar(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId, @Param(COLLEGE_REGISTRAR_ID) BigInteger collegeRegistrarId);

    @Query(value = FETCH_USER_SPECIFIC_STATUS_WISE_COUNT_QUERY_FOR_SMC,nativeQuery = true)
    List<IStatusWiseCount> fetchUserSpecificStatusWiseCountForSmc(@Param(APPLICATION_TYPE_ID) BigInteger applicationTypeId, @Param(GROUP_ID) BigInteger groupId, @Param(SMC_PROFILE_ID) BigInteger smcProfileId);

}
