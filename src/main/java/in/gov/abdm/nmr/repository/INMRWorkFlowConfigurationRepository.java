package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.dto.NextGroupTO;
import in.gov.abdm.nmr.mapper.INextGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import in.gov.abdm.nmr.entity.NmrWorkFlowConfiguration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface INMRWorkFlowConfigurationRepository extends JpaRepository<NmrWorkFlowConfiguration, BigInteger> {

    @Query("SELECT wc.assignTo.id as assignTo,wc.workFlowStatus.id as workFlowStatusId FROM NmrWorkFlowConfiguration wc " +
            "WHERE wc.applicationType.id=:applicationTypeId " +
            "AND wc.actionPerformedBy.id=:actionPerformedBy " +
            "AND wc.action.id=:actionId")
    INextGroup getNextGroup(@Param("applicationTypeId") BigInteger applicationTypeId,
                             @Param("actionPerformedBy") BigInteger actionPerformedBy,
                             @Param("actionId") BigInteger actionId);
}
