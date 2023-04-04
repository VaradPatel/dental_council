package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkFlow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IWorkFlowRepository extends JpaRepository<WorkFlow, BigInteger> {

    WorkFlow findByRequestId(String requestId);

    @Query(value = "Select * from work_flow wf  where wf.work_flow_status_id = 1 and hp_profile_id = :hpProfileId", nativeQuery = true)
    List<WorkFlow> findPendingWorkflow(BigInteger hpProfileId);
    
    @Query(value = "Select * from work_flow wf where wf.work_flow_status_id = 2 and hp_profile_id = :hpProfileId order by updated_at desc limit 1 offset 0 ", nativeQuery = true)
    WorkFlow findApprovedWorkflow(BigInteger hpProfileId);

    @Query(value = "Select * from work_flow wf where wf.work_flow_status_id in (1,3) and wf.application_type_id != :applicationTypeId and  wf.hp_profile_id = :hpProfileId", nativeQuery = true)
    WorkFlow findAnyActiveWorkflowWithDifferentApplicationType(BigInteger hpProfileId, BigInteger applicationTypeId);

    @Query(value = "Select * from work_flow wf  where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<WorkFlow> findWorkflowById(BigInteger hpProfileId);

    @Query(value = "Select * from work_flow wf where wf.hp_profile_id = :hpProfileId order by created_at desc limit 1", nativeQuery = true)
    WorkFlow findLastWorkFlowForHealthProfessional(BigInteger hpProfileId);
}
