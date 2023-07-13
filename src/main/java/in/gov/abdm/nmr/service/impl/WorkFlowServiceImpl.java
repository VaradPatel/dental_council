package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.util.NMRUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.QUALIFICATION_STATUS_REJECTED;
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
    private IForeignQualificationDetailRepository foreignQualificationDetailRepository;

    @Autowired
    INotificationService notificationService;

    @Autowired
    IWorkflowPostProcessorService workflowPostProcessorService;

    @Autowired
    private IUserDaoService userDetailService;

    @Autowired
    IDashboardRepository iDashboardRepository;

    private static final List<BigInteger> APPLICABLE_POST_PROCESSOR_WORK_FLOW_STATUSES = List.of(WorkflowStatus.APPROVED.getId(), WorkflowStatus.BLACKLISTED.getId(), WorkflowStatus.SUSPENDED.getId());

    @Override
    @Transactional
    public void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException, InvalidRequestException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User user = userDetailService.findByUsername(userName, userType);

        validateAndThrowExceptionForNonVerifierUsers(user);

        WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
        HpProfile hpProfile = iHpProfileRepository.findById(requestTO.getHpProfileId()).orElse(new HpProfile());
        INextGroup iNextGroup = null;
        Dashboard dashboard = null;
        if (workFlow == null) {
            log.debug("Proceeding to create a new Workflow entry since there are no existing entries with the given request_id");
            validateRequestPayloadForApplicationCreation(requestTO);

            log.debug("Fetching the Next Group to assign this request to and the work_flow_status using Application type, Application sub type, Actor and Action");
            iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId(), requestTO.getApplicationSubTypeId());
            workFlow = buildNewWorkFlow(requestTO, iNextGroup, hpProfile, user);
            iWorkFlowRepository.save(workFlow);
            log.debug("Work Flow Creation Successful");
            dashboard = new Dashboard();
        } else {
            dashboard = iDashboardRepository.findByRequestId(workFlow.getRequestId());
            log.debug("Proceeding to update the existing Workflow entry since there is an existing entry with the given request_id");
            if (!workFlow.getApplicationType().getId().equals(requestTO.getApplicationTypeId()) || workFlow.getCurrentGroup() == null || (!Group.SMC.getId().equals(requestTO.getActorId()) && !workFlow.getCurrentGroup().getId().equals(requestTO.getActorId()))) {
                log.debug("Invalid Request since either the given application type matches the fetched application type from the workflow or current group fetched from the workflow is null or current group id fetched from the workflow matches the given actor id");
                throw new InvalidRequestException();
            }
            log.debug("Fetching the Next Group to assign this request to and the work_flow_status using Application type, Application sub type, Actor and Action");
            iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId(), requestTO.getApplicationSubTypeId());
            if (iNextGroup != null) {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(requestTO.getActionId()).orElseThrow(WorkFlowException::new));
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).orElseThrow(WorkFlowException::new) : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).orElseThrow(WorkFlowException::new));
                workFlow.setRemarks(requestTO.getRemarks());
                workFlow.setUserId(user);
                log.debug("Work Flow Updating Successful");
            } else {
                throw new WorkFlowException(NMRError.WORK_FLOW_EXCEPTION.getCode(), NMRError.WORK_FLOW_EXCEPTION.getMessage());
            }
        }

        log.debug("Saving an entry in the work_flow_audit table");
        iWorkFlowAuditRepository.save(buildNewWorkFlowAudit(requestTO, iNextGroup, hpProfile, user));
        log.debug("Initiating a notification to indicate the change of status.");

        updateDashboardDetail(requestTO, workFlow, iNextGroup, dashboard);

        if (isLastStepOfWorkFlow(iNextGroup)) {
            performPostWorkFlowTask(requestTO, workFlow, hpProfile, iNextGroup);
        }
        sendNotificationsOnStatusChanges(user, workFlow, hpProfile);
    }

    private void performPostWorkFlowTask(WorkFlowRequestTO requestTO, WorkFlow workFlow, HpProfile hpProfile, INextGroup iNextGroup) throws WorkFlowException {
        if (APPLICABLE_POST_PROCESSOR_WORK_FLOW_STATUSES.contains(workFlow.getWorkFlowStatus().getId())) {
            log.debug("Performing Post Workflow updates since either the Last step of Workflow is reached or work_flow_status is Approved/Suspended/Blacklisted ");
            workflowPostProcessorService.performPostWorkflowUpdates(requestTO, hpProfile, iNextGroup);
        } else if (ApplicationType.ADDITIONAL_QUALIFICATION.getId().equals(workFlow.getApplicationType().getId())
                && WorkflowStatus.REJECTED.getId().equals(workFlow.getWorkFlowStatus().getId())) {

            QualificationDetails qualificationDetails = qualificationDetailRepository.findByRequestId(workFlow.getRequestId());
            if(qualificationDetails!=null) {
                qualificationDetails.setIsVerified(NMRConstants.QUALIFICATION_STATUS_REJECTED);
            }

            ForeignQualificationDetails foreignQualificationDetails = foreignQualificationDetailRepository.findByRequestId(requestTO.getRequestId());
            if(foreignQualificationDetails!=null) {
                foreignQualificationDetails.setIsVerified(QUALIFICATION_STATUS_REJECTED);
            }
        }
    }

    private void sendNotificationsOnStatusChanges(User user, WorkFlow workFlow, HpProfile hpProfile) {
        try {
            if(!ApplicationType.HP_MODIFICATION.getId().equals(workFlow.getApplicationType().getId())) {
                if (hpProfile.getUser().isSmsNotificationEnabled() && hpProfile.getUser().isEmailNotificationEnabled()) {
                    notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName() + getVerifierNameForNotification(user), hpProfile.getUser().getMobileNumber(), hpProfile.getUser().getEmail());
                } else if (hpProfile.getUser().isSmsNotificationEnabled()) {
                    notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName() + getVerifierNameForNotification(user), hpProfile.getUser().getMobileNumber(), null);
                } else if (hpProfile.getUser().isEmailNotificationEnabled()) {
                    notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName() + getVerifierNameForNotification(user), null, hpProfile.getUser().getEmail());
                }
            }
        } catch (Exception exception) {
            log.debug("error occurred while sending notification:" + exception.getLocalizedMessage());
        }
    }

    private static void validateRequestPayloadForApplicationCreation(WorkFlowRequestTO requestTO) throws InvalidRequestException {
        if (Group.HEALTH_PROFESSIONAL.getId().equals(requestTO.getActorId())) {
            if (!ApplicationType.getAllHpApplicationTypeIds().contains(requestTO.getApplicationTypeId()) ||
                    !Action.SUBMIT.getId().equals(requestTO.getActionId())) {
                log.debug("Health Professional is the Actor but either Action is not Submit or Application type is invalid");
                throw new InvalidRequestException();
            }
        } else if ((Group.SMC.getId().equals(requestTO.getActorId()) || Group.NMC.getId().equals(requestTO.getActorId())) && !Action.PERMANENT_SUSPEND.getId().equals(requestTO.getActionId()) &&
                !Action.TEMPORARY_SUSPEND.getId().equals(requestTO.getActionId()) && !Action.SUBMIT.getId().equals(requestTO.getActionId())) {
            log.debug("SMC or NMC is the Actor but Action is not Temporary Suspend or Permanent Suspend");
            throw new InvalidRequestException();
        }
    }

    /**
     * Admin users cannot take any actions on any workflow related applications.
     * @param user the user.
     * @throws InvalidRequestException
     */
    private static void validateAndThrowExceptionForNonVerifierUsers(User user) throws InvalidRequestException {
        if (user != null) {
            BigInteger userTypeId = user.getUserType().getId();
            if ((UserTypeEnum.COLLEGE.getId().equals(userTypeId) && UserSubTypeEnum.COLLEGE_ADMIN.getId().equals(userTypeId))
                    || (UserTypeEnum.NMC.getId().equals(userTypeId) && UserSubTypeEnum.NMC_ADMIN.getId().equals(userTypeId))) {
                throw new InvalidRequestException();
            }
        }
    }

    private String getVerifierNameForNotification(User user) {
        String verifier = "";
        if (user != null) {
            if (user.getUserType().getId().equals(UserTypeEnum.COLLEGE.getId())) {
                verifier = NMRConstants.VERIFIER_COLLEGE;
            } else if (user.getUserType().getId().equals(UserTypeEnum.SMC.getId())) {
                verifier = NMRConstants.VERIFIER_SMC;
            } else if (user.getUserType().getId().equals(UserTypeEnum.NMC.getId())) {
                verifier = NMRConstants.VERIFIER_NMC;
            } else if (user.getUserType().getId().equals(UserTypeEnum.NBE.getId())) {
                verifier = NMRConstants.VERIFIER_NBE;
            } else if (user.getUserType().getId().equals(UserTypeEnum.SYSTEM.getId())) {
                verifier = NMRConstants.VERIFIER_SYSTEM;
            }
        }
        return verifier;
    }

    private void updateDashboardDetail(WorkFlowRequestTO requestTO, WorkFlow workFlow, INextGroup iNextGroup, Dashboard dashboard) {
        dashboard.setApplicationTypeId(requestTO.getApplicationTypeId());
        dashboard.setRequestId(requestTO.getRequestId());
        dashboard.setHpProfileId(requestTO.getHpProfileId());
        dashboard.setWorkFlowStatusId(workFlow.getWorkFlowStatus().getId());
        if (NMRUtil.isVoluntarySuspension(workFlow)) {
            dashboard.setNmcStatus(Action.APPROVED.getId());
            dashboard.setSmcStatus(Action.APPROVED.getId());
        } else {
            setDashboardStatus(requestTO.getActionId(), requestTO.getActorId(), dashboard);
            if (!isLastStepOfWorkFlow(iNextGroup)) {
                if ((Group.COLLEGE.getId().equals(requestTO.getActorId()) || Group.NBE.getId().equals(requestTO.getActorId()))  && Action.APPROVED.getId().equals(requestTO.getActionId())) {
                    dashboard.setSmcStatus(DashboardStatus.COLLEGE_NBE_VERIFIED.getId());
                } else {
                    setDashboardStatus(DashboardStatus.PENDING.getId(), iNextGroup.getAssignTo(), dashboard);
                }
            }
        }
        dashboard.setCreatedAt(Timestamp.from(Instant.now()));
        dashboard.setUpdatedAt(Timestamp.from(Instant.now()));
        iDashboardRepository.save(dashboard);

    }

    /**
     * Updates the dashboard status. In case SMC decides to override the college application, then application will be revoked from college and application status will be set to null and smc status will be updated as per action performed by smc.
     *
     * @param actionPerformedId the action peformed
     * @param userGroup the user group of user
     * @param dashboard the dashboard entity.
     */
    private static void setDashboardStatus(BigInteger actionPerformedId, BigInteger userGroup, Dashboard dashboard){
        BigInteger dashboardStatusId = DashboardStatus.getDashboardStatus(Action.getAction(actionPerformedId).getStatus()).getId();
        if (Group.SMC.getId().equals(userGroup)) {
            dashboard.setSmcStatus(dashboardStatusId);
            if(DashboardStatus.PENDING.getId().equals(dashboard.getCollegeStatus())){
                dashboard.setCollegeStatus(null);
            }
        } else if (Group.NMC.getId().equals(userGroup)) {
            dashboard.setNmcStatus(dashboardStatusId);
        } else if (Group.COLLEGE.getId().equals(userGroup)) {
            dashboard.setCollegeStatus(dashboardStatusId);
        } else if (Group.NBE.getId().equals(userGroup)) {
            dashboard.setNbeStatus(dashboardStatusId);
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
    public void assignQueriesBackToQueryCreator(String requestId) throws WorkFlowException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();

        User user = null;
        if (userName != null) {
            user = userDetailService.findByUsername(userName, userType);
        }
        WorkFlow workflow = iWorkFlowRepository.findByRequestId(requestId);
        UserGroup previousGroup = workflow.getPreviousGroup();
        UserGroup currentGroup = workflow.getCurrentGroup();
        workflow.setCurrentGroup(previousGroup);
        workflow.setPreviousGroup(currentGroup);
        workflow.setAction(iActionRepository.findById(Action.SUBMIT.getId()).orElseThrow(WorkFlowException::new));
        workflow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(WorkflowStatus.PENDING.getId()).orElseThrow(WorkFlowException::new));
        workflow.setUserId(user);

        WorkFlowAudit workFlowAudit = WorkFlowAudit.builder().requestId(requestId)
                .hpProfile(workflow.getHpProfile())
                .applicationType(workflow.getApplicationType())
                .createdBy(workflow.getCreatedBy())
                .action(iActionRepository.findById(Action.SUBMIT.getId()).orElseThrow(WorkFlowException::new))
                .workFlowStatus(iWorkFlowStatusRepository.findById(WorkflowStatus.PENDING.getId()).orElseThrow(WorkFlowException::new))
                .previousGroup(currentGroup)
                .currentGroup(previousGroup)
                .startDate(workflow.getStartDate())
                .endDate(workflow.getEndDate())
                .userId(user)
                .build();
        iWorkFlowAuditRepository.save(workFlowAudit);

        Dashboard dashboard = iDashboardRepository.findByRequestId(requestId);
        if (previousGroup.getId().equals(Group.SMC.getId())) {
            dashboard.setSmcStatus(DashboardStatus.PENDING.getId());
        } else if (previousGroup.getId().equals(Group.NMC.getId())) {
            dashboard.setNmcStatus(DashboardStatus.PENDING.getId());
        } else if (previousGroup.getId().equals(Group.COLLEGE.getId())) {
            dashboard.setCollegeStatus(DashboardStatus.PENDING.getId());
        } else if (previousGroup.getId().equals(Group.NBE.getId())) {
            dashboard.setNbeStatus(DashboardStatus.PENDING.getId());
        }
        dashboard.setWorkFlowStatusId(DashboardStatus.PENDING.getId());
        iDashboardRepository.save(dashboard);
    }

    private WorkFlow buildNewWorkFlow(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile, User user) throws WorkFlowException {

        UserGroup actorGroup = iGroupRepository.findById(requestTO.getActorId()).orElseThrow(WorkFlowException::new);

        return WorkFlow.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).orElseThrow(WorkFlowException::new))
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).orElseThrow(WorkFlowException::new))
                .hpProfile(hpProfile)
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).orElseThrow(WorkFlowException::new))
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? coalesce(iGroupRepository.findById(iNextGroup.getAssignTo()).orElseThrow(WorkFlowException::new), null) : null)
                .startDate(requestTO.getStartDate())
                .endDate(requestTO.getEndDate())
                .remarks(requestTO.getRemarks())
                .userId(user)
                .build();
    }

    private WorkFlowAudit buildNewWorkFlowAudit(WorkFlowRequestTO requestTO, INextGroup iNextGroup, HpProfile hpProfile, User user) throws WorkFlowException {

        UserGroup actorGroup = iGroupRepository.findById(requestTO.getActorId()).orElseThrow(WorkFlowException::new);
        return WorkFlowAudit.builder().requestId(requestTO.getRequestId())
                .applicationType(iApplicationTypeRepository.findById(requestTO.getApplicationTypeId()).orElseThrow(WorkFlowException::new))
                .createdBy(actorGroup)
                .action(iActionRepository.findById(requestTO.getActionId()).orElseThrow(WorkFlowException::new))
                .hpProfile(hpProfile)
                .workFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).orElseThrow(WorkFlowException::new))
                .previousGroup(actorGroup)
                .currentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).orElseThrow(WorkFlowException::new) : null)
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
