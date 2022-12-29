package in.gov.abdm.nmr.repository;

import static in.gov.abdm.nmr.util.NMRConstants.APPLICATION_STATUS_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.USER_SUB_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.USER_TYPE;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.dto.IStatusWiseCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;

@Repository
public interface IDashboardRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY)
    List<IStatusWiseCount> fetchHpStatusWiseCountByAppStatusAndUserType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType);

    @Query(value = FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY)
    List<BigInteger> fetchTotalCountByAppStatusAndUserType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType);

    @Query(value = FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY)
    List<IStatusWiseCount> fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType, @Param(USER_SUB_TYPE) String userSubType);

    @Query(value = FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY)
    List<BigInteger> fetchTotalCountByAppStatusAndUserTypeAndSubType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType, @Param(USER_SUB_TYPE) String userSubType);

}
