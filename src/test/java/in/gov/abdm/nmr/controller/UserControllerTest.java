package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IMasterDataService;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = UserController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = UserController.class)
@ActiveProfiles(profiles = "local")
@SuppressWarnings("java:S1192")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IMasterDataService masterDataService;

    @MockBean
    private IUserService userService;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser
    void testToggleNotification() throws Exception {
        NotificationToggleRequestTO notificationToggleRequestTO = new NotificationToggleRequestTO();
        List<NotificationToggleTO> notificationToggleTOS = new ArrayList<>();
        NotificationToggleTO notificationToggleTO = new NotificationToggleTO();
        notificationToggleTO.setMode(NMRConstants.EMAIL);
        notificationToggleTO.setIsEnabled(true);
        notificationToggleTOS.add(notificationToggleTO);
        notificationToggleRequestTO.setNotificationToggles(notificationToggleTOS);

        List<NotificationToggleResponseTO> toggleResponseTOS = new ArrayList<>();
        NotificationToggleResponseTO response = new NotificationToggleResponseTO();
        response.setUserId(USER_ID);
        response.setMode(NMRConstants.EMAIL);
        response.setEnabled(true);
        toggleResponseTOS.add(response);
        when(userService.toggleNotification(any(NotificationToggleRequestTO.class))).thenReturn(toggleResponseTOS);
        mockMvc.perform(put(ProtectedPaths.PATH_USER_NOTIFICATION_ENABLED)
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(notificationToggleRequestTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].user_id").value(USER_ID))
                .andExpect(jsonPath("$.[0].mode").value(NMRConstants.EMAIL))
                .andExpect(jsonPath("$.[0].enabled").value(true));
    }

    @Test
    @WithMockUser
    void testSmcProfile() throws Exception {
        SMCProfileTO smcProfile = new SMCProfileTO();
        smcProfile.setId(ID);
        smcProfile.setUserId(USER_ID);
        smcProfile.setFirstName(FIRST_NAME);
        smcProfile.setLastName(LAST_NAME);
        smcProfile.setMiddleName(MIDDLE_NAME);
        StateMedicalCouncilTO stateMedicalCouncil = new StateMedicalCouncilTO();
        stateMedicalCouncil.setId(SMC_ID);
        stateMedicalCouncil.setCode(SMC_CODE);
        stateMedicalCouncil.setName(SMC_NAME);
        smcProfile.setStateMedicalCouncil(stateMedicalCouncil);
        smcProfile.setNdhmEnrollment(ENROLLED_NUMBER);
        smcProfile.setEnrolledNumber(ENROLLED_NUMBER);
        smcProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        smcProfile.setEmailId(EMAIL_ID);
        smcProfile.setMobileNo(MOBILE_NUMBER);
        when(userService.getSmcProfile(any(BigInteger.class))).thenReturn(smcProfile);
        mockMvc.perform(get("/smc/user/1").with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.state_medical_council.id").value(SMC_ID))
                .andExpect(jsonPath("$.state_medical_council.code").value(SMC_CODE))
                .andExpect(jsonPath("$.state_medical_council.name").value(SMC_NAME))
                .andExpect(jsonPath("$.ndhm_enrollment").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.enrolled_number").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }


    @Test
    @WithMockUser
    void testNmcProfile() throws Exception {
        NmcProfileTO nmcProfileTO = new NmcProfileTO();
        nmcProfileTO.setId(ID);
        nmcProfileTO.setUserId(USER_ID);
        nmcProfileTO.setFirstName(FIRST_NAME);
        nmcProfileTO.setLastName(LAST_NAME);
        nmcProfileTO.setMiddleName(MIDDLE_NAME);
        StateMedicalCouncilTO stateMedicalCouncil = new StateMedicalCouncilTO();
        stateMedicalCouncil.setId(SMC_ID);
        stateMedicalCouncil.setCode(SMC_CODE);
        stateMedicalCouncil.setName(SMC_NAME);
        nmcProfileTO.setStateMedicalCouncil(stateMedicalCouncil);
        nmcProfileTO.setNdhmEnrollment(ENROLLED_NUMBER);
        nmcProfileTO.setEnrolledNumber(ENROLLED_NUMBER);
        nmcProfileTO.setDisplayName(PROFILE_DISPLAY_NAME);
        nmcProfileTO.setEmailId(EMAIL_ID);
        nmcProfileTO.setMobileNo(MOBILE_NUMBER);
        when(userService.getNmcProfile(any(BigInteger.class))).thenReturn(nmcProfileTO);
        mockMvc.perform(get("/nmc/user/1").with(user(TEST_USER)).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.state_medical_council.id").value(SMC_ID))
                .andExpect(jsonPath("$.state_medical_council.code").value(SMC_CODE))
                .andExpect(jsonPath("$.state_medical_council.name").value(SMC_NAME))
                .andExpect(jsonPath("$.ndhm_enrollment").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.enrolled_number").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }

    @Test
    @WithMockUser
    void testNbeProfile() throws Exception {
        NbeProfileTO nbeProfile = new NbeProfileTO();
        nbeProfile.setId(ID);
        nbeProfile.setUserId(USER_ID);
        nbeProfile.setFirstName(FIRST_NAME);
        nbeProfile.setLastName(LAST_NAME);
        nbeProfile.setMiddleName(MIDDLE_NAME);
        nbeProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nbeProfile.setEmailId(EMAIL_ID);
        nbeProfile.setMobileNo(MOBILE_NUMBER);
        when(userService.getNbeProfile(any(BigInteger.class))).thenReturn(nbeProfile);
        mockMvc.perform(get("/nbe/user/1")
                        .with(user(TEST_USER))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }

    @Test
    @WithMockUser
    void testUpdateSMCProfile() throws Exception {
        SMCProfileTO smcProfile = new SMCProfileTO();
        smcProfile.setId(ID);
        smcProfile.setUserId(USER_ID);
        smcProfile.setFirstName(FIRST_NAME);
        smcProfile.setLastName(LAST_NAME);
        smcProfile.setMiddleName(MIDDLE_NAME);
        smcProfile.setStateMedicalCouncil(new StateMedicalCouncilTO(SMC_ID, SMC_CODE, SMC_NAME));
        smcProfile.setNdhmEnrollment(NDHM_ENROLLMENT_NUMBER);
        smcProfile.setEnrolledNumber(NDHM_ENROLLMENT_NUMBER);
        smcProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        smcProfile.setEmailId(EMAIL_ID);
        smcProfile.setMobileNo(MOBILE_NUMBER);
        when(userService.updateSmcProfile(any(BigInteger.class), any(SMCProfileTO.class))).thenReturn(smcProfile);
        mockMvc.perform(put("/smc/user/1")
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(smcProfile))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.state_medical_council.id").value(SMC_ID))
                .andExpect(jsonPath("$.state_medical_council.code").value(SMC_CODE))
                .andExpect(jsonPath("$.state_medical_council.name").value(SMC_NAME))
                .andExpect(jsonPath("$.ndhm_enrollment").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.enrolled_number").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }

    @Test
    @WithMockUser
    void testUpdateNMCProfile() throws Exception {
        NmcProfileTO nmcProfile = new NmcProfileTO();
        nmcProfile.setId(ID);
        nmcProfile.setUserId(USER_ID);
        nmcProfile.setFirstName(FIRST_NAME);
        nmcProfile.setLastName(LAST_NAME);
        nmcProfile.setMiddleName(MIDDLE_NAME);
        nmcProfile.setStateMedicalCouncil(new StateMedicalCouncilTO(SMC_ID, SMC_CODE, SMC_NAME));
        nmcProfile.setNdhmEnrollment(NDHM_ENROLLMENT_NUMBER);
        nmcProfile.setEnrolledNumber(NDHM_ENROLLMENT_NUMBER);
        nmcProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nmcProfile.setEmailId(EMAIL_ID);
        nmcProfile.setMobileNo(MOBILE_NUMBER);
        when(userService.updateNmcProfile(any(BigInteger.class), any(NmcProfileTO.class))).thenReturn(nmcProfile);
        mockMvc.perform(put("/nmc/user/1").with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(nmcProfile))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.state_medical_council.id").value(SMC_ID))
                .andExpect(jsonPath("$.state_medical_council.code").value(SMC_CODE))
                .andExpect(jsonPath("$.state_medical_council.name").value(SMC_NAME))
                .andExpect(jsonPath("$.ndhm_enrollment").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.enrolled_number").value(ENROLLED_NUMBER))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }

    @Test
    @WithMockUser
    void testUpdateNBEProfile() throws Exception {
        NbeProfileTO nbeProfile = new NbeProfileTO();
        nbeProfile.setId(ID);
        nbeProfile.setUserId(USER_ID);
        nbeProfile.setFirstName(FIRST_NAME);
        nbeProfile.setLastName(LAST_NAME);
        nbeProfile.setMiddleName(MIDDLE_NAME);
        nbeProfile.setDisplayName(PROFILE_DISPLAY_NAME);
        nbeProfile.setEmailId(EMAIL_ID);
        nbeProfile.setMobileNo(MOBILE_NUMBER);
        when(userService.updateNbeProfile(any(BigInteger.class), any(NbeProfileTO.class))).thenReturn(nbeProfile);
        mockMvc.perform(put("/nbe/user/1").with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(nbeProfile))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ID))
                .andExpect(jsonPath("$.user_id").value(USER_ID))
                .andExpect(jsonPath("$.first_name").value(FIRST_NAME))
                .andExpect(jsonPath("$.last_name").value(LAST_NAME))
                .andExpect(jsonPath("$.middle_name").value(MIDDLE_NAME))
                .andExpect(jsonPath("$.display_name").value(PROFILE_DISPLAY_NAME))
                .andExpect(jsonPath("$.email_id").value(EMAIL_ID))
                .andExpect(jsonPath("$.mobile_no").value(MOBILE_NUMBER));
    }


    @Test
    @WithMockUser
    void testRetrieveUser() throws Exception {
        RetrieveUserRequestTo retrieveUserRequestTo = new RetrieveUserRequestTo();
        retrieveUserRequestTo.setTransactionId(TRANSACTION_ID);
        retrieveUserRequestTo.setContact(MOBILE_NUMBER);
        when(userService.retrieveUser(nullable(RetrieveUserRequestTo.class))).thenReturn(EMAIL_ID);
        mockMvc.perform(post(NMRConstants.RETRIEVE_USER).with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(retrieveUserRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string(EMAIL_ID));
    }


    @Test
    @WithMockUser
    void testVerifyEmail() throws Exception {
        VerifyEmailTo verifyEmailTo = new VerifyEmailTo();
        verifyEmailTo.setToken(TEMP_TOKN);
        ResponseMessageTo responseMessageTo = new ResponseMessageTo();
        responseMessageTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        when(userService.verifyEmail(any(VerifyEmailTo.class))).thenReturn(responseMessageTo);
        mockMvc.perform(post(NMRConstants.VERIFY_EMAIL).with(user(TEST_USER))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(verifyEmailTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE));
    }
}