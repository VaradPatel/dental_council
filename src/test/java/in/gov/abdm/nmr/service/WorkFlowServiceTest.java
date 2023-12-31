package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.Dashboard;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import in.gov.abdm.nmr.entity.WorkFlowStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.TemplateException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.WorkFlowServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkFlowServiceTest {

    @InjectMocks
    WorkFlowServiceImpl workFlowService;

    @Mock
    IUserDaoService userDaoService;

    @Mock
    IWorkFlowRepository iWorkFlowRepository;

    @Mock
    IHpProfileRepository hpProfileRepository;

    @Mock
    INMRWorkFlowConfigurationRepository inmrWorkFlowConfigurationRepository;

    @Mock
    IActionRepository actionRepository;

    @Mock
    IUserGroupRepository userGroupRepository;

    @Mock
    IApplicationTypeRepository applicationTypeRepository;

    @Mock
    IWorkFlowAuditRepository workFlowAuditRepository;

    @Mock
    IDashboardRepository dashboardRepository;

    @Mock
    INotificationService notificationService;

    @Mock
    IWorkFlowStatusRepository workFlowStatusRepository;

    @Mock
    IActionRepository iActionRepository;

    @Mock
    IWorkFlowStatusRepository iWorkFlowStatusRepository;

    @Mock
    IUserDaoService userDetailService;
    @Mock
    IDashboardRepository iDashboardRepository;

    @Test
    void testInitiateSubmissionFlow() throws WorkFlowException, InvalidRequestException, TemplateException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDetailService.findByUsername(anyString(), any(BigInteger.class))).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId()));
        when(iWorkFlowRepository.findByRequestId(anyString())).thenReturn(null);
        when(hpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(inmrWorkFlowConfigurationRepository.getNextGroup(any(BigInteger.class), any(BigInteger.class), any(BigInteger.class), any(BigInteger.class))).thenReturn(getNextGroup());
        when(userGroupRepository.findById(Group.SMC.getId())).thenReturn(Optional.of(getUserGroup(Group.SMC.getId())));
        when(userGroupRepository.findById(Group.HEALTH_PROFESSIONAL.getId())).thenReturn(Optional.of(getUserGroup(Group.HEALTH_PROFESSIONAL.getId())));
        when(applicationTypeRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getApplicationType()));
        when(iWorkFlowRepository.save(any())).thenReturn(WorkFlow.builder().workFlowStatus(WorkFlowStatus.builder().id(WorkflowStatus.PENDING.getId()).build()).build());
        when(workFlowAuditRepository.save(any())).thenReturn(new WorkFlowAudit());
        when(iActionRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(new in.gov.abdm.nmr.entity.Action()));
        when(iWorkFlowStatusRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(new in.gov.abdm.nmr.entity.WorkFlowStatus()));
        workFlowService.initiateSubmissionWorkFlow(getWorkFlowRequestTO());

        Mockito.verify(workFlowAuditRepository, Mockito.times(1)).save(any(WorkFlowAudit.class));

    }

    private in.gov.abdm.nmr.entity.ApplicationType getApplicationType() {
        in.gov.abdm.nmr.entity.ApplicationType applicationType = new in.gov.abdm.nmr.entity.ApplicationType();
        applicationType.setId(ApplicationType.HP_REGISTRATION.getId());
        applicationType.setName(ApplicationType.HP_REGISTRATION.name());
        return applicationType;
    }

    private in.gov.abdm.nmr.entity.Action getAction() {
        in.gov.abdm.nmr.entity.Action action = new in.gov.abdm.nmr.entity.Action();
        action.setId(Action.SUBMIT.getId());
        action.setName(Action.SUBMIT.name());
        return action;
    }

    private INextGroup getNextGroup() {
        return new INextGroup() {
            @Override
            public BigInteger getAssignTo() {
                return Group.SMC.getId();
            }

            @Override
            public BigInteger getWorkFlowStatusId() {
                return WorkflowStatus.PENDING.getId();
            }
        };
    }

    public WorkFlowRequestTO getWorkFlowRequestTO() {
        WorkFlowRequestTO workFlowRequestTO = new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(REQUEST_ID);
        workFlowRequestTO.setHpProfileId(getHpProfile().getId());
        workFlowRequestTO.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
        workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
        workFlowRequestTO.setActionId(Action.SUBMIT.getId());
        workFlowRequestTO.setRemarks("Doctor Registration");
        workFlowRequestTO.setApplicationSubTypeId(BigInteger.valueOf(1));
        return workFlowRequestTO;
    }

    @Test
    void testIsAnyActiveWorkflowWithOtherApplicationType() {
        when(iWorkFlowRepository.findAnyActiveWorkflowWithDifferentApplicationType(any(BigInteger.class), anyList())).thenReturn(new WorkFlow());
        boolean anyActiveWorkflowWithOtherApplicationType = workFlowService.isAnyActiveWorkflowWithOtherApplicationType(ID, List.of(ID));
        assertFalse(anyActiveWorkflowWithOtherApplicationType);
    }

    @Test
    void testIsAnyActiveWorkflowForHealthProfessional() {
        when(iWorkFlowRepository.findPendingWorkflow(any(BigInteger.class))).thenReturn(List.of(getWorkFlow()));
        boolean anyActiveWorkflowForHealthProfessional = workFlowService.isAnyActiveWorkflowForHealthProfessional(ID);
        assertTrue(anyActiveWorkflowForHealthProfessional);
    }

    @Test
    void testAssignQueriesBackToQueryCreator() throws WorkFlowException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(iWorkFlowRepository.findByRequestId(anyString())).thenReturn(getWorkFlow());
        when(iActionRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(new in.gov.abdm.nmr.entity.Action()));
        when(iWorkFlowStatusRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(new in.gov.abdm.nmr.entity.WorkFlowStatus()));
        when(iDashboardRepository.findByRequestId(anyString())).thenReturn(new Dashboard());
        when(iDashboardRepository.save(any(Dashboard.class))).thenReturn(new Dashboard());
        when(hpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        workFlowService.assignQueriesBackToQueryCreator(REQUEST_ID,ID);
        Mockito.verify(iWorkFlowStatusRepository, Mockito.times(2)).findById(any(BigInteger.class));
    }

    @Test
    void testIsAnyApprovedWorkflowForHealthProfessionalShouldReturnTrueWhenDataFoundForHealthProfessionalId() {
        when(iWorkFlowRepository.findApprovedWorkflow(any(BigInteger.class))).thenReturn(getWorkFlow());
        boolean anyApprovedWorkflowForHealthProfessional = workFlowService.isAnyApprovedWorkflowForHealthProfessional(ID);
        assertTrue(anyApprovedWorkflowForHealthProfessional);
    }

}
