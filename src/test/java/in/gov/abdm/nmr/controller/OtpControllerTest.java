package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.AfterEach;
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

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = OtpController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = OtpController.class)
@ActiveProfiles(profiles = "local")
@EnableWebMvc
public class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IOtpService otpService;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @WithMockUser
    void testGenerateOtp() throws Exception {
        OtpGenerateRequestTo otpGenerateRequestTo = new OtpGenerateRequestTo();
        otpGenerateRequestTo.setContact(MOBILE_NUMBER);
        otpGenerateRequestTo.setType(TYPE);
        OTPResponseMessageTo otpResponseMessageTo = new OTPResponseMessageTo();
        otpResponseMessageTo.setTransactionId(TRANSACTION_ID);
        otpResponseMessageTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        otpResponseMessageTo.setSentOn(MOBILE_NUMBER);
        when(otpService.generateOtp(nullable(OtpGenerateRequestTo.class))).thenReturn(otpResponseMessageTo);
        mockMvc.perform(post("/notification/send-otp").with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(otpGenerateRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE))
                .andExpect(jsonPath("$.sentOn").value(MOBILE_NUMBER));
    }
/*
    @Test
    @WithMockUser
    void testValidateOtp() throws Exception {
        OtpValidateRequestTo otpValidateRequestTo = new OtpValidateRequestTo();
        otpValidateRequestTo.setTransactionId(TRANSACTION_ID);
        otpValidateRequestTo.setContact(MOBILE_NUMBER);
        otpValidateRequestTo.setType(TYPE);
        otpValidateRequestTo.setOtp(OTP);
        OtpValidateResponseTo otpValidateResponseTo = new OtpValidateResponseTo(
                new OtpValidateMessageTo(NMRConstants.SUCCESS_RESPONSE, TRANSACTION_ID, TYPE));
        when(otpService.validateOtp(nullable(OtpValidateRequestTo.class), any(Boolean.class)))
                .thenReturn(otpValidateResponseTo);
        mockMvc.perform(post("/notification/verify-otp").with(user("123"))
                        .with(csrf())
                        .content(objectMapper.writeValueAsBytes(otpValidateRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message.status").value(NMRConstants.SUCCESS_RESPONSE))
                .andExpect(jsonPath("$.message.transaction_id").value(TRANSACTION_ID))
                .andExpect(jsonPath("$.message.type").value(TYPE));
    }*/
}