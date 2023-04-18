package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkFlowAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IWorkFlowAuditRepository extends JpaRepository<WorkFlowAudit, BigInteger> {

    /**
     * Fetches the application details from the work_flow_audit table for the given request ID
     * and returns a list of WorkFlowAudit objects sorted by created_at in ascending order.
     *
     * @param requestId the ID of the request to fetch the application details for
     * @return a list of WorkFlowAudit objects containing the application details for the request
     */
    @Query(value = "Select * from work_flow_audit where request_id = :requestId order by created_at asc ", nativeQuery = true)
    List<WorkFlowAudit> fetchApplicationDetails(String requestId);
}
