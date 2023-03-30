package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;

@Service
@Slf4j
public class WorkFlowServiceImpl implements IWorkFlowService {

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
    private IQualificationDetailRepository qualificationDetailRepository;

    @Autowired
    INotificationService notificationService;

    @Autowired
    IWorkflowPostProcessorService workflowPostProcessorService;

    @Autowired
    private IUserDaoService userDetailService;

    private static final List APPLICABLE_POST_PROCESSOR_WORK_FLOW_STATUSES = List.of(WorkflowStatus.APPROVED.getId(), WorkflowStatus.BLACKLISTED.getId(), WorkflowStatus.SUSPENDED.getId());

    @Override
    @Transactional
    public void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;

        user = userDetailService.findByUsername(userName);

        if(user!=null) {
            if (UserTypeEnum.COLLEGE.getCode().equals(user.getUserType().getId())) {
                if (UserSubTypeEnum.COLLEGE.getCode().equals(user.getUserSubType().getId())) {
                    throw new WorkFlowException("Invalid Request", HttpStatus.BAD_REQUEST);
                }
            }
        }

        WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
        HpProfile hpProfile = iHpProfileRepository.findById(requestTO.getHpProfileId()).orElse(new HpProfile());
        INextGroup iNextGroup = null;
        if (workFlow == null) {
            log.debug("Proceeding to create a new Workflow entry since there are no existing entries with the given request_id");
            if (Group.HEALTH_PROFESSIONAL.getId().equals(requestTO.getActorId())) {
                if (!ApplicationType.getAllHpApplicationTypeIds().contains(requestTO.getApplicationTypeId()) ||
                        !Action.SUBMIT.getId().equals(requestTO.getActionId()))
                    log.debug("Health Professional is the Actor but either Action is not Submit or Application type is invalid");
                    throw new WorkFlowException("Invalid Request", HttpStatus.BAD_REQUEST);
            } else if (Group.SMC.getId().equals(requestTO.getActorId()) || Group.NMC.getId().equals(requestTO.getActorId())) {
                if (//!ApplicationType.HP_TEMPORARY_SUSPENSION.equals(requestTO.getApplicationTypeId()) ||
                    //!ApplicationType.HP_PERMANENT_SUSPENSION.equals(requestTO.getApplicationTypeId()) ||
                        !Action.PERMANENT_SUSPEND.getId().equals(requestTO.getActionId()) &&
                                !Action.TEMPORARY_SUSPEND.getId().equals(requestTO.getActionId())) {
                    log.debug("SMC or NMC is the Actor but Action is not Temporary Suspend or Permanent Suspend");
                    throw new WorkFlowException("Invalid Request", HttpStatus.BAD_REQUEST);
                }
            }

            log.debug("Fetching the Next Group to assign this request to and the work_flow_status using Application type, Application sub type, Actor and Action");
            iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId(), requestTO.getApplicationSubTypeId());
            workFlow = buildNewWorkFlow(requestTO, iNextGroup, hpProfile, user);
            iWorkFlowRepository.save(workFlow);
            log.debug("Work Flow Creation Successful");
        } else {
            log.debug("Proceeding to update the existing Workflow entry since there is an existing entry with the given request_id");
            if (!workFlow.getApplicationType().getId().equals(requestTO.getApplicationTypeId()) || workFlow.getCurrentGroup() == null || !workFlow.getCurrentGroup().getId().equals(requestTO.getActorId())) {
                log.debug("Invalid Request since either the given application type matches the fetched application type from the workflow or current group fetched from the workflow is null or current group id fetched from the workflow matches the given actor id");
                throw new WorkFlowException("Invalid Request", HttpStatus.BAD_REQUEST);
            }
            log.debug("Fetching the Next Group to assign this request to and the work_flow_status using Application type, Application sub type, Actor and Action");
            iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(workFlow.getApplicationType().getId(), workFlow.getCurrentGroup().getId(), requestTO.getActionId(), requestTO.getApplicationSubTypeId());
            if (iNextGroup != null) {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(requestTO.getActionId()).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
                workFlow.setRemarks(requestTO.getRemarks());
                workFlow.setUserId(user);
                log.debug("Work Flow Updation Successful");
            } else {
                throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
            }
        }
        if (isLastStepOfWorkFlow(iNextGroup) &&
                APPLICABLE_POST_PROCESSOR_WORK_FLOW_STATUSES.contains(workFlow.getWorkFlowStatus().getId())) {
            log.debug("Performing Post Workflow updates since either the Last step of Workflow is reached or work_flow_status is Approved/Suspended/Blacklisted ");
            workflowPostProcessorService.performPostWorkflowUpdates(requestTO, hpProfile, iNextGroup);
        }
        log.debug("Saving an entry in the work_flow_audit table");
        iWorkFlowAuditRepository.save(buildNewWorkFlowAudit(requestTO, iNextGroup, hpProfile, user));
        log.debug("Initiating a notification to indicate the change of status.");
        notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName(), workFlow.getHpProfile().getMobileNumber(), workFlow.getHpProfile().getEmailId());

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
    public void assignQueriesBackToQueryCreator(String requestId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;
        if (userName != null) {
            user = userDetailService.findByUsername(userName);
        }
        WorkFlow workflow = iWorkFlowRepository.findByRequestId(requestId);
        UserGroup previousGroup = workflow.getPreviousGroup();
        UserGroup currentGroup = workflow.getCurrentGroup();
        workflow.setCurrentGroup(previousGroup);
        workflow.setPreviousGroup(currentGroup);
        workflow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(WorkflowStatus.PENDING.getId()).get());
        workflow.setUserId(user);

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
                .userId(user)
                .build();
        iWorkFlowAuditRepository.save(workFlowAudit);
    }

    private WorkFlow buildNewCollegeWorkFlow(String requestId, BigInteger applicationTypeId, BigInteger actionId, BigInteger actorId, INextGroup iNextGroup, User user) {
        UserGroup actorGroup = iGroupRepository.findById(actorId).get();
        return WorkFlow.builder().requestId(requestId)
                .applicationType(iApplicationTypeRepository.findById(applicationTypeId).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(actionId).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iGroupRepository.findById(iNextGroup.getAssignTo()).get())
                .userId(user)
                .build();
    }

    private WorkFlowAudit buildNewCollegeWorkFlowAudit(String requestId, BigInteger applicationTypeId, BigInteger actionId, BigInteger actorId, INextGroup iNextGroup, User user) {
        UserGroup actorGroup = iGroupRepository.findById(actorId).get();
        return WorkFlowAudit.builder().requestId(requestId)
                .applicationType(iApplicationTypeRepository.findById(applicationTypeId).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(actionId).get())
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null)
                .userId(user)
                .build();
    }

    private WorkFlow buildNewWorkFlow(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile, User user) {

        UserGroup actorGroup = iGroupRepository.findById(requestTO.getActorId()).get();

        return WorkFlow.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).get())
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).get())
                .hpProfile(hpProfile)
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get())
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? coalesce(iGroupRepository.findById(iNextGroup.getAssignTo()).get(), null) : null)
                .startDate(requestTO.getStartDate())
                .endDate(requestTO.getEndDate())
                .remarks(requestTO.getRemarks())
                .userId(user)
                .build();
    }

    private WorkFlowAudit buildNewWorkFlowAudit(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile, User user) {

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
                .userId(user)
                .build();
    }


    public boolean isAnyApprovedWorkflowForHealthProfessional(BigInteger hpProfileId) {
        return iWorkFlowRepository.findApprovedWorkflow(hpProfileId) != null;
    }
}
