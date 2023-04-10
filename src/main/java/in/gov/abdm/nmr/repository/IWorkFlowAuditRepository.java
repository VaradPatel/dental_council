package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IWorkFlowAuditRepository extends JpaRepository<WorkFlowAudit, BigInteger> {

    WorkFlow findByRequestId(String requestId);

    @Query(value = "Select * from work_flow wf  where wf.work_flow_status_id = 1 and hp_profile_id = :hpProfileId", nativeQuery = true)
    List<WorkFlow> findPendingWorkflow(BigInteger hpProfileId);

    @Query(value = "Select * from work_flow_master wf where wf.work_flow_status_id = 2 and hp_profile_id = :hpProfileId order by updated_at desc limit 1 offset 0 ", nativeQuery = true)
    WorkFlow findApprovedWorkflow(BigInteger hpProfileId);

    @Query(value = "Select * from work_flow_master wf where wf.work_flow_status_id in (1,3) and wf.application_type_id != :applicationTypeId and  wf.hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkFlow findAnyActiveWorkflowWithDifferentApplicationType(BigInteger hpProfileId, BigInteger applicationTypeId);

    @Query(value = "Select * from work_flow_master wf  where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<WorkFlowAudit> findWorkflowById(BigInteger hpProfileId);

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
