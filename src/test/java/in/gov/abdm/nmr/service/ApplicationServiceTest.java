package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.ApplicationRequestTo;
import in.gov.abdm.nmr.dto.WorkFlowRequestTO;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileStatus;
import in.gov.abdm.nmr.entity.RequestCounter;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.service.impl.ApplicationServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.sql.Timestamp;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @InjectMocks
    ApplicationServiceImpl applicationService;

    @Mock
    IHpProfileRepository hpProfileRepository;

    @Mock
    IRequestCounterService requestCounterService;

    @Mock
    IUserDaoService userDaoService;

    @Mock
    IWorkFlowService workFlowService;

    @Test
    void testSuspendRequestShouldThrowNmrExceptionWhenHpProfileIsNotApproved(){
        when(hpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(getHpProfile());
        assertThrows(NmrException.class, () -> applicationService.suspendRequest(getApplicationRequestTo()));
    }

    @Test
    void testSuspendRequestShouldCreateSuspensionRequestForApprovedProfile() throws WorkFlowException, NmrException {
        HpProfile hpProfile = getHpProfile();
        hpProfile.setHpProfileStatus(HpProfileStatus.builder().id(in.gov.abdm.nmr.enums.HpProfileStatus.APPROVED.getId()).build());
        RequestCounter requestCounter = RequestCounter.builder().counter(BigInteger.valueOf(1))
                .applicationType(in.gov.abdm.nmr.entity.ApplicationType.builder().id(ApplicationType.HP_TEMPORARY_SUSPENSION.getId()).requestPrefixId("NMR300").build()).build();

        when(hpProfileRepository.findHpProfileById(any(BigInteger.class))).thenReturn(hpProfile);
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(requestCounter);
        when(userDaoService.findByUsername(anyString())).thenReturn(getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getCode()));
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        doNothing().when(workFlowService).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
        applicationService.suspendRequest(getApplicationRequestTo());
        Mockito.verify(workFlowService, Mockito.times(1)).initiateSubmissionWorkFlow(any(WorkFlowRequestTO.class));
    }

    private ApplicationRequestTo getApplicationRequestTo() {
        ApplicationRequestTo applicationRequestTo = new ApplicationRequestTo();
        applicationRequestTo.setApplicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
        applicationRequestTo.setHpProfileId(ID);
        applicationRequestTo.setRemarks("Suspend");
        applicationRequestTo.setFromDate(Timestamp.valueOf("2023-12-01 00:00:00"));
        applicationRequestTo.setToDate(Timestamp.valueOf("2024-12-01 00:00:00"));
        return applicationRequestTo;
    }

}
