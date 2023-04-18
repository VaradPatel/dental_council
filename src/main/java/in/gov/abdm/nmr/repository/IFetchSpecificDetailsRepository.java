package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Repository
public interface IFetchSpecificDetailsRepository extends JpaRepository<WorkFlow, BigInteger> {
    @Query(value = FETCH_DETAILS_FOR_LISTING_QUERY, nativeQuery = true)
    List<IFetchSpecificDetails> fetchDetailsForListing(@Param(GROUP_NAME) String groupName, @Param(APPLICATION_TYPE_NAME) String applicationTypeName, @Param(WORK_FLOW_STATUS) String workFlowStatus);

    @Query(value = FETCH_DETAILS_WITH_PENDING_STATUS_FOR_LISTING_QUERY, nativeQuery = true)
    List<IFetchSpecificDetails> fetchDetailsWithPendingStatusForListing(@Param(GROUP_NAME) String groupName, @Param(APPLICATION_TYPE_NAME) String applicationTypeName, @Param(WORK_FLOW_STATUS) String workFlowStatus);

    @Query(value = FETCH_DETAILS_WITH_APPROVED_STATUS_FOR_LISTING_QUERY, nativeQuery = true)
    List<IFetchSpecificDetails> fetchDetailsWithApprovedStatusForListing(@Param(GROUP_NAME) String groupName, @Param(APPLICATION_TYPE_NAME) String applicationTypeName, @Param(WORK_FLOW_STATUS) String workFlowStatus);

}