package in.gov.abdm.nmr.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.INotificationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.service.IElasticsearchDaoService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import org.springframework.util.CollectionUtils;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;

@Service
public class WorkFlowServiceImpl implements IWorkFlowService {

    private static final Logger LOGGER = LogManager.getLogger();

    /**
     * Injecting IWorkFlowRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowRepository iWorkFlowRepository;

    /**
     * Injecting IWorkFlowRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private IWorkFlowAuditRepository iWorkFlowAuditRepository;

    /**
     * Injecting INMRWorkFlowConfigurationRepository bean instead of an explicit object creation to achieve
     * Singleton principle
     */
    @Autowired
    private INMRWorkFlowConfigurationRepository inmrWorkFlowConfigurationRepository;

    @Autowired
    private IApplicationTypeRepository iApplicationTypeRepository;

    @Autowired
    private IUserGroupRepository iGroupRepository;

    @Autowired
    private IActionRepository iActionRepository;

    @Autowired
    private IHpProfileRepository iHpProfileRepository;

    @Autowired
    private IWorkFlowStatusRepository iWorkFlowStatusRepository;

    @Autowired
    private IHpProfileStatusRepository hpProfileStatusRepository;

    @Autowired
    private IElasticsearchDaoService elasticsearchDaoService;

    @Autowired
    private IQualificationDetailRepository qualificationDetailRepository;

    @Autowired
    INotificationService notificationService;

    @Override
    @Transactional
    public void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException {
        INextGroup iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId());
        if (iNextGroup != null) {
            HpProfile hpProfile = iHpProfileRepository.findById(requestTO.getHpProfileId()).get();
            WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
            if (workFlow == null) {
                workFlow = buildNewWorkFlow(requestTO, iNextGroup, hpProfile);
                iWorkFlowRepository.save(workFlow);
            } else {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(requestTO.getActionId()).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
                workFlow.setRemarks(requestTO.getRemarks());
            }
            if (isLastStepOfWorkFlow(iNextGroup)) {
                updateHealthProfessionalDetails(requestTO, iNextGroup, hpProfile);
                addOrUpdateToHpElasticIndex(iNextGroup, hpProfile);
            }
            iWorkFlowAuditRepository.save(buildNewWorkFlowAudit(requestTO, iNextGroup, hpProfile));
//            notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName(), workFlow.getHpProfile().getMobileNumber(), workFlow.getHpProfile().getEmailId());

        } else {
            throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    private void updateHealthProfessionalDetails(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {
        if (!ApplicationType.QUALIFICATION_ADDITION.getId().equals(requestTO.getApplicationTypeId())) {
            hpProfile.setHpProfileStatus(hpProfileStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
        }
        List<QualificationDetails> qualificationDetails = qualificationDetailRepository.findByRequestId(requestTO.getRequestId());
        qualificationDetails.forEach(qualificationDetail -> qualificationDetail.setIsVerified(1));
    }

    private void addOrUpdateToHpElasticIndex(INextGroup iNextGroup, HpProfile hpProfile) throws WorkFlowException {
        try {
            elasticsearchDaoService.indexHP(hpProfile.getId());
        } catch (ElasticsearchException | IOException e) {
            LOGGER.error("Exception while indexing HP", e);
            throw new WorkFlowException("Exception while indexing HP", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean isAnyActiveWorkflowWithOtherApplicationType(BigInteger hpProfileId, BigInteger applicationTypeId) {
        return iWorkFlowRepository.findAnyActiveWorkflowWithDifferentApplicationType(hpProfileId, applicationTypeId) == null;
    }


    private boolean isLastStepOfWorkFlow(INextGroup nextGroup) {
        return nextGroup.getAssignTo() == null;
    }

    public boolean isAnyActiveWorkflowForHealthProfessional(BigInteger hpProfileId) {
        return !CollectionUtils.isEmpty(iWorkFlowRepository.findPendingWorkflow(hpProfileId));
    }

    @Override
    public void initiateCollegeRegistrationWorkFlow(String requestId, BigInteger applicationTypeId, BigInteger actorId, BigInteger actionId) throws WorkFlowException {
        INextGroup iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(applicationTypeId, actorId, actionId);
        if (iNextGroup != null) {
            WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestId);
            if (workFlow == null) {
                WorkFlow collegeWorkFlow = buildNewCollegeWorkFlow(requestId, applicationTypeId, actionId, actorId, iNextGroup);
                iWorkFlowRepository.save(collegeWorkFlow);
            } else {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(actionId).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
            }
            iWorkFlowAuditRepository.save(buildNewCollegeWorkFlowAudit(requestId, applicationTypeId, actionId, actorId, iNextGroup));
            notificationService.sendNotificationOnStatusChangeForCollege(workFlow.getApplicationType().getName(), workFlow.getAction().getName(), workFlow.getHpProfile().getMobileNumber(), workFlow.getHpProfile().getEmailId());

        } else {
            throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public void assignQueriesBackToQueryCreator(String requestId) {
        WorkFlow workflow = iWorkFlowRepository.findByRequestId(requestId);
        UserGroup previousGroup = workflow.getPreviousGroup();
        UserGroup currentGroup = workflow.getCurrentGroup();
        workflow.setCurrentGroup(previousGroup);
        workflow.setPreviousGroup(currentGroup);
        workflow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(WorkflowStatus.PENDING.getId()).get());

        WorkFlowAudit workFlowAudit = WorkFlowAudit.builder().requestId(requestId)
                .hpProfile(workflow.getHpProfile())
                .applicationType(workflow.getApplicationType())
                .createdBy(workflow.getCreatedBy())
                .action(iActionRepository.findById(Action.SUBMIT.getId()).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(WorkflowStatus.PENDING.getId()).get())
                .previousGroup(currentGroup)
                .currentGroup(previousGroup)
                .startDate(workflow.getStartDate())
                .endDate(workflow.getEndDate())
                .build();
        iWorkFlowAuditRepository.save(workFlowAudit);
    }

    private WorkFlow buildNewCollegeWorkFlow(String requestId, BigInteger applicationTypeId, BigInteger actionId, BigInteger actorId, INextGroup iNextGroup) {
        UserGroup actorGroup = iGroupRepository.findById(actorId).get();
        return WorkFlow.builder().requestId(requestId)
                .applicationType(iApplicationTypeRepository.findById(applicationTypeId).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(actionId).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iGroupRepository.findById(iNextGroup.getAssignTo()).get())
                .build();
    }

    private WorkFlowAudit buildNewCollegeWorkFlowAudit(String requestId, BigInteger applicationTypeId, BigInteger actionId, BigInteger actorId, INextGroup iNextGroup) {
        UserGroup actorGroup = iGroupRepository.findById(actorId).get();
        return WorkFlowAudit.builder().requestId(requestId)
                .applicationType(iApplicationTypeRepository.findById(applicationTypeId).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(actionId).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null)
                .build();
    }

    private WorkFlow buildNewWorkFlow(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {

        UserGroup actorGroup = iGroupRepository.findById(requestTO.getActorId()).get();

        return WorkFlow.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).get())
                .hpProfile(hpProfile)
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(coalesce(iGroupRepository.findById(iNextGroup.getAssignTo()).get(), null))
                .startDate(requestTO.getStartDate())
                .endDate(requestTO.getEndDate())
                .remarks(requestTO.getRemarks())
                .build();
    }

    private WorkFlowAudit buildNewWorkFlowAudit(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile) {

        UserGroup actorGroup = iGroupRepository.findById(requestTO.getActorId()).get();
        return WorkFlowAudit.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).get())
                .hpProfile(hpProfile)
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null)
                .startDate(requestTO.getStartDate())
                .endDate(requestTO.getEndDate())
                .remarks(requestTO.getRemarks())
                .build();
    }


    public boolean isAnyApprovedWorkflowForHealthProfessional(BigInteger hpProfileId) {
        return iWorkFlowRepository.findApprovedWorkflow(hpProfileId) != null;
    }
}
