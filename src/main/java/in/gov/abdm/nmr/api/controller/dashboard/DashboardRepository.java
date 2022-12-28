package in.gov.abdm.nmr.api.controller.dashboard;

import in.gov.abdm.nmr.api.controller.dashboard.to.StatusWiseCount;
import in.gov.abdm.nmr.db.sql.domain.hp_verification_status.HpVerificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.api.constant.NMRConstants.*;

@Repository
public interface DashboardRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY)
    List<StatusWiseCount> fetchHpStatusWiseCountByAppStatusAndUserType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType);

    @Query(value = FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_TYPE_QUERY)
    List<BigInteger> fetchTotalCountByAppStatusAndUserType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType);

    @Query(value = FETCH_HP_STATUS_WISE_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY)
    List<StatusWiseCount> fetchHpStatusWiseCountByAppStatusAndUserTypeAndSubType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType, @Param(USER_SUB_TYPE) String userSubType);

    @Query(value = FETCH_TOTAL_COUNT_BY_APP_STATUS_AND_USER_SUB_TYPE_QUERY)
    List<BigInteger> fetchTotalCountByAppStatusAndUserTypeAndSubType(@Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(USER_TYPE) String userType, @Param(USER_SUB_TYPE) String userSubType);

}
