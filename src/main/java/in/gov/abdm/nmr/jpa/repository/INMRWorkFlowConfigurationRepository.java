package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.NmrWorkFlowConfiguration;
import in.gov.abdm.nmr.mapper.INextGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;

public interface INMRWorkFlowConfigurationRepository extends JpaRepository<NmrWorkFlowConfiguration, BigInteger> {

    @Query("SELECT wc.assignTo.id as assignTo,wc.workFlowStatus.id as workFlowStatusId FROM NmrWorkFlowConfiguration wc " +
            "WHERE wc.applicationType.id=:applicationTypeId " +
            "AND wc.actionPerformedBy.id=:actionPerformedBy " +
            "AND wc.action.id=:actionId " +
            "AND (:applicationSubTypeId is null OR wc.applicationSubType.id=:applicationSubTypeId) ")
    INextGroup getNextGroup(@Param("applicationTypeId") BigInteger applicationTypeId,
                            @Param("actionPerformedBy") BigInteger actionPerformedBy,
                            @Param("actionId") BigInteger actionId,
                            @Param("applicationSubTypeId") BigInteger applicationSubTypeId);
}
