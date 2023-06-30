package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.enums.Action;
import in.gov.abdm.nmr.enums.ApplicationType;
import in.gov.abdm.nmr.exception.NmrExceptionAdvice;
import in.gov.abdm.nmr.repository.IWorkFlowRepository;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IApplicationService;
import in.gov.abdm.nmr.service.IRequestCounterService;
import in.gov.abdm.nmr.service.IWorkFlowService;
import in.gov.abdm.nmr.util.CommonTestData;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static in.gov.abdm.nmr.util.NMRConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = ApplicationController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = {NmrExceptionAdvice.class, ApplicationController.class})
@ActiveProfiles(profiles = "local")
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IApplicationService applicationService;
    @MockBean
    private IWorkFlowService iWorkFlowService;
    @MockBean
    private IRequestCounterService requestCounterService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private IWorkFlowRepository iWorkFlowRepository;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser
    void testSuspendHealthProfessionalShouldSuspendHealthProfessional() throws Exception {
        ApplicationRequestTo applicationRequestTo = new ApplicationRequestTo();
        applicationRequestTo.setApplicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
        applicationRequestTo.setToDate(Timestamp.valueOf(LocalDateTime.now()));
        applicationRequestTo.setHpProfileId(CommonTestData.ID);
        applicationRequestTo.setActionId(Action.SUBMIT.getId());
        applicationRequestTo.setRemarks("Remarks");

        SuspendRequestResponseTo suspendRequestResponseTo = new SuspendRequestResponseTo();
        suspendRequestResponseTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        suspendRequestResponseTo.setProfileId(CommonTestData.ID.toString());

        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(any(BigInteger.class))).thenReturn(false);
        when(applicationService.suspendRequest(any(ApplicationRequestTo.class))).thenReturn(suspendRequestResponseTo);

        mockMvc.perform(post(ProtectedPaths.SUSPENSION_REQUEST_URL)
                        .with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(applicationRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testSuspendHealthProfessionalShouldThrowWhenActiveWorkFlowForHealthProfessional() throws Exception {
        ApplicationRequestTo applicationRequestTo = new ApplicationRequestTo();
        applicationRequestTo.setApplicationTypeId(ApplicationType.HP_PERMANENT_SUSPENSION.getId());
        applicationRequestTo.setToDate(Timestamp.valueOf(LocalDateTime.now()));
        applicationRequestTo.setHpProfileId(CommonTestData.ID);
        applicationRequestTo.setActionId(Action.SUBMIT.getId());
        applicationRequestTo.setRemarks("Remarks");

        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(nullable(BigInteger.class))).thenReturn(true);

        mockMvc.perform(post(ProtectedPaths.SUSPENSION_REQUEST_URL)
                        .with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(applicationRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testReactivateHealthProfessional() throws Exception {
        ReactivateRequestResponseTo reactivateRequestResponseTo = new ReactivateRequestResponseTo();
        reactivateRequestResponseTo.setProfileId("1");
        reactivateRequestResponseTo.setSelfReactivation(true);
        reactivateRequestResponseTo.setMessage(SUCCESS_RESPONSE);
        when(applicationService.reactivateRequest(any(MultipartFile.class),any(ApplicationRequestTo.class))).thenReturn(reactivateRequestResponseTo);
        mockMvc.perform(post(ProtectedPaths.REACTIVATE_REQUEST_URL).with(user(TEST_USER)).with(csrf())
                        .content(objectMapper.writeValueAsBytes(new ApplicationRequestTo())).accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile_id").value("1"))
                .andExpect(jsonPath("$.self_reactivation").value(true))
                .andExpect(jsonPath("$.message").value(SUCCESS_RESPONSE));

    }

    @Test
    @WithMockUser
    void testReactivationRecordsOfHealthProfessionalsToNmc() throws Exception {
        when(applicationService.getReactivationRecordsOfHealthProfessionalsToNmc(nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class)))
                .thenReturn(new ReactivateHealthProfessionalResponseTO());
        mockMvc.perform(get(ProtectedPaths.REACTIVATE_REQUEST_URL).with(user(TEST_USER))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }


    @Test
    @WithMockUser
    void testTrackApplicationDetailsShouldFetchApplicationDetailsForHealthProfessional() throws Exception {
        when(applicationService.fetchApplicationDetailsForHealthProfessional(nullable(BigInteger.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class)))
                .thenReturn(new HealthProfessionalApplicationResponseTo());
        mockMvc.perform(get("/health-professional/1/applications").with(user(TEST_USER))
                .accept(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testTrackStatusDetailsShouldFetchApplicationDetails() throws Exception {
        when(applicationService.fetchApplicationDetails(nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class)))
                .thenReturn(new HealthProfessionalApplicationResponseTo());
        mockMvc.perform(get(APPLICATION_REQUEST_URL)
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testApplicationDetailShouldReturnApplicationDetail() throws Exception {
        when(applicationService.fetchApplicationDetail(nullable(String.class))).thenReturn(new ApplicationDetailResponseTo());
        mockMvc.perform(get("/applications/1").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testExecuteActionOnHealthProfessionalShouldThrowWorkFlowCreationFailException() throws Exception {
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(getWorkFlowRequestTO());
        when(iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(any(BigInteger.class), any(BigInteger.class))).thenReturn(false);
        mockMvc.perform(patch(HEALTH_PROFESSIONAL_ACTION)
                        .with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(getWorkFlowRequestTO()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void testExecuteActionOnHealthProfessionalShouldExecuteActionOnHealthProfessionalSuccessfully() throws Exception {
        when(iWorkFlowService.isAnyActiveWorkflowWithOtherApplicationType(any(BigInteger.class), any(BigInteger.class))).thenReturn(true);
        when(iWorkFlowService.isAnyActiveWorkflowForHealthProfessional(any(BigInteger.class))).thenReturn(false);
        when(requestCounterService.incrementAndRetrieveCount(any(BigInteger.class))).thenReturn(getRequestCounter());
        doNothing().when(iWorkFlowService).initiateSubmissionWorkFlow(getWorkFlowRequestTO());
        mockMvc.perform(patch(HEALTH_PROFESSIONAL_ACTION)
                        .with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(getWorkFlowRequestTO()))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }
}