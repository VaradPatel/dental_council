package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.mapper.IFetchSpecificDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
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

    @Query(value = "SELECT id FROM hp_profile where registration_id IN (SELECT registration_id FROM hp_profile WHERE id = :hpId) ", nativeQuery = true)
    List<BigInteger> getHpProfileIds(BigInteger hpId);

    @Query(value = "SELECT request_id, application_type_id, created_at, work_flow_status_id ,current_group_id, " +
            " extract(day from now()- created_at) pendency_days " +
            " FROM work_flow where hp_profile_id IN (:hpProfileIds)", nativeQuery = true)
    List<Tuple>fetchTrackApplicationDetails(List<BigInteger> hpProfileIds, Pageable pagination);
}