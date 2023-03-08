package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.WorkflowStatus;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.service.IWorkflowPostProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.NMRUtil.coalesce;

@Service
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
    ICollegeRepository collegeRepository;

    @Autowired
    private IUserDaoService userDetailService;

    @Override
    @Transactional
    public void initiateSubmissionWorkFlow(WorkFlowRequestTO requestTO) throws WorkFlowException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;
        if (userName != null) {
            user = userDetailService.findByUsername(userName);
        }

        INextGroup iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(requestTO.getApplicationTypeId(), requestTO.getActorId(), requestTO.getActionId());
        if (iNextGroup != null) {
            HpProfile hpProfile = iHpProfileRepository.findById(requestTO.getHpProfileId()).get();
            WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestTO.getRequestId());
            if (workFlow == null) {
                workFlow = buildNewWorkFlow(requestTO, iNextGroup, hpProfile, user);
                iWorkFlowRepository.save(workFlow);
            } else {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(requestTO.getActionId()).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
                workFlow.setRemarks(requestTO.getRemarks());
                workFlow.setUserId(user);
            }
            if (isLastStepOfWorkFlow(iNextGroup)) {
                workflowPostProcessorService.performPostWorkflowUpdates(requestTO,hpProfile,iNextGroup);
            }
            iWorkFlowAuditRepository.save(buildNewWorkFlowAudit(requestTO, iNextGroup, hpProfile, user));
            notificationService.sendNotificationOnStatusChangeForHP(workFlow.getApplicationType().getName(), workFlow.getAction().getName(), workFlow.getHpProfile().getMobileNumber(), workFlow.getHpProfile().getEmailId());

        } else {
            throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
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
    @Transactional
    public void initiateCollegeRegistrationWorkFlow(String requestId, BigInteger applicationTypeId, BigInteger actorId, BigInteger actionId) throws WorkFlowException {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = null;
        if (userName != null) {
            user = userDetailService.findByUsername(userName);
        }

        INextGroup iNextGroup = inmrWorkFlowConfigurationRepository.getNextGroup(applicationTypeId, actorId, actionId);
        if (iNextGroup != null) {
            WorkFlow workFlow = iWorkFlowRepository.findByRequestId(requestId);
            if (workFlow == null) {
                WorkFlow collegeWorkFlow = buildNewCollegeWorkFlow(requestId, applicationTypeId, actionId, actorId, iNextGroup, user);
                workFlow = iWorkFlowRepository.save(collegeWorkFlow);
            } else {
                workFlow.setUpdatedAt(null);
                workFlow.setAction(iActionRepository.findById(actionId).get());
                workFlow.setPreviousGroup(workFlow.getCurrentGroup());
                workFlow.setCurrentGroup(iNextGroup.getAssignTo() != null ? iGroupRepository.findById(iNextGroup.getAssignTo()).get() : null);
                workFlow.setWorkFlowStatus(iWorkFlowStatusRepository.findById(iNextGroup.getWorkFlowStatusId()).get());
                workFlow.setUserId(user);
            }
            iWorkFlowAuditRepository.save(buildNewCollegeWorkFlowAudit(requestId, applicationTypeId, actionId, actorId, iNextGroup,user));
            College college = collegeRepository.findCollegeByRequestId(requestId);
            if(college != null && college.getEmailId() != null) {
                notificationService.sendNotificationOnStatusChangeForCollege(workFlow.getApplicationType().getName(), workFlow.getAction().getName(), null, college.getEmailId());
            }

        } else {
            throw new WorkFlowException("Next Group Not Found", HttpStatus.BAD_REQUEST);
        }
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
