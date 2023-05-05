package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.Dashboard;
import in.gov.abdm.nmr.entity.WorkFlow;
import in.gov.abdm.nmr.entity.WorkFlowAudit;
import in.gov.abdm.nmr.entity.WorkFlowStatus;
import in.gov.abdm.nmr.enums.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.INextGroup;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import in.gov.abdm.nmr.service.impl.WorkFlowServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Test
    void testInitiateSubmissionFlow() throws WorkFlowException, InvalidRequestException {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        when(iWorkFlowRepository.findByRequestId(anyString())).thenReturn(null);
        when(hpProfileRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getHpProfile()));
        when(inmrWorkFlowConfigurationRepository.getNextGroup(any(BigInteger.class),any(BigInteger.class),any(BigInteger.class),any(BigInteger.class))).thenReturn(getNextGroup());
        when(actionRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getAction()));
        when(userGroupRepository.findById(Group.SMC.getId())).thenReturn(Optional.of(getUserGroup(Group.SMC.getId())));
        when(userGroupRepository.findById(Group.HEALTH_PROFESSIONAL.getId())).thenReturn(Optional.of(getUserGroup(Group.HEALTH_PROFESSIONAL.getId())));
        when(applicationTypeRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getApplicationType()));
        when(iWorkFlowRepository.save(any())).thenReturn(WorkFlow.builder().workFlowStatus(WorkFlowStatus.builder().id(WorkflowStatus.PENDING.getId()).build()).build());
        when(workFlowAuditRepository.save(any())).thenReturn(new WorkFlowAudit());
        when(dashboardRepository.save(any())).thenReturn(new Dashboard());
        when(notificationService.sendNotificationOnStatusChangeForHP(anyString(),anyString(),anyString(),anyString())).thenReturn(new ResponseMessageTo());
        when(workFlowStatusRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(WorkFlowStatus.builder().id(WorkflowStatus.PENDING.getId()).build()));
        workFlowService.initiateSubmissionWorkFlow(getWorkFlowRequestTO());

        Mockito.verify(dashboardRepository, Mockito.times(1)).save(any(Dashboard.class));

    }

    private in.gov.abdm.nmr.entity.ApplicationType getApplicationType(){
        in.gov.abdm.nmr.entity.ApplicationType applicationType =  new in.gov.abdm.nmr.entity.ApplicationType();
        applicationType.setId(ApplicationType.HP_REGISTRATION.getId());
        applicationType.setName(ApplicationType.HP_REGISTRATION.name());
        return applicationType;
    }

    private in.gov.abdm.nmr.entity.Action getAction() {
        in.gov.abdm.nmr.entity.Action action =  new in.gov.abdm.nmr.entity.Action();
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

    public WorkFlowRequestTO getWorkFlowRequestTO(){
        WorkFlowRequestTO workFlowRequestTO =  new WorkFlowRequestTO();
        workFlowRequestTO.setRequestId(REQUEST_ID);
        workFlowRequestTO.setHpProfileId(getHpProfile().getId());
        workFlowRequestTO.setApplicationTypeId(ApplicationType.HP_REGISTRATION.getId());
        workFlowRequestTO.setActorId(Group.HEALTH_PROFESSIONAL.getId());
        workFlowRequestTO.setActionId(Action.SUBMIT.getId());
        workFlowRequestTO.setRemarks("Doctor Registration");
        workFlowRequestTO.setApplicationSubTypeId(BigInteger.valueOf(1));
        return workFlowRequestTO;
    }
}
