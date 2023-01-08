package in.gov.abdm.nmr.repository;

import static in.gov.abdm.nmr.entity.HpProfile_.HP_PROFILE_STATUS;
import static in.gov.abdm.nmr.util.NMRConstants.APPLICATION_STATUS_TYPE;
import static in.gov.abdm.nmr.util.NMRConstants.FETCH_DETAILS_FOR_LISTING_BY_USER_TYPE_QUERY;
import static in.gov.abdm.nmr.util.NMRConstants.USER_TYPE;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.entity.HpVerificationStatus;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;

@Repository
public interface IFetchSpecificDetailsRepository extends JpaRepository<HpVerificationStatus, BigInteger> {

    @Query(value = FETCH_DETAILS_FOR_LISTING_BY_USER_TYPE_QUERY)
    List<IFetchSpecificDetails> fetchDetailsForListingByUserType(@Param(USER_TYPE) String userType, @Param(APPLICATION_STATUS_TYPE) String applicationStatusType, @Param(HP_PROFILE_STATUS) String hpProfileStatus);

}
