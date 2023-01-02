package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.entity.HpProfile_.HP_PROFILE_STATUS;
import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
public interface IFetchSpecificDetailsRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_DETAILS_FOR_LISTING_BY_USER_TYPE_QUERY)
    List<IFetchSpecificDetails> fetchDetailsForListingByUserType(@Param(USER_TYPE) String userType, @Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(HP_PROFILE_STATUS) String hpProfileStatus);

    @Query(value = FETCH_DETAILS_FOR_LISTING_BY_USER_TYPE_AND_SUB_TYPE_QUERY)
    List<IFetchSpecificDetails> fetchDetailsForListingByUserTypeAndSubType(@Param(USER_TYPE) String userType, @Param(USER_SUB_TYPE) String userSubType, @Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(HP_PROFILE_STATUS) String hpProfileStatus);

}
