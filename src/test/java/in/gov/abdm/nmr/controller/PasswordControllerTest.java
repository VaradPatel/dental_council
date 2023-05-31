package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IPasswordService;
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

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PasswordController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = PasswordController.class)
@ActiveProfiles(profiles = "local")
@SuppressWarnings("java:S1192")
class PasswordControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    IPasswordService passwordService;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }


    @Test
    @WithMockUser
    void testSetNewPassword() throws Exception {
        SetNewPasswordTo newPasswordTo = new SetNewPasswordTo();
        newPasswordTo.setToken(TEMP_TOKEN);
        newPasswordTo.setPassword(TEST_PSWD);
        ResponseMessageTo responseMessageTo = new ResponseMessageTo();
        responseMessageTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        when(passwordService.setNewPassword(newPasswordTo)).thenReturn(responseMessageTo);
        mockMvc.perform(post(NMRConstants.SET_PASSWORD).with(user(TEST_USER))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(newPasswordTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE));
    }

    @Test
    @WithMockUser
    void testResetPassword() throws Exception {
        ResetPasswordRequestTo requestTo = new ResetPasswordRequestTo();
        requestTo.setUsername(TEST_USER);
        requestTo.setPassword(TEST_PSWD);
        requestTo.setTransactionId(TRANSACTION_ID);
        ResponseMessageTo responseMessageTo = new ResponseMessageTo();
        responseMessageTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        when(passwordService.resetPassword(requestTo)).thenReturn(responseMessageTo);
        mockMvc.perform(post(NMRConstants.RESET_PASSWORD).with(user(TEST_USER))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE));
    }

    @Test
    @WithMockUser
    void testChangePassword() throws Exception {
        ChangePasswordRequestTo requestTo = new ChangePasswordRequestTo();
        requestTo.setUserId(ID);
        requestTo.setOldPassword(TEST_PSWD);
        requestTo.setNewPassword(TEST_PSWD);
        ResponseMessageTo responseMessageTo = new ResponseMessageTo();
        responseMessageTo.setMessage(NMRConstants.SUCCESS_RESPONSE);
        when(passwordService.changePassword(requestTo)).thenReturn(responseMessageTo);
        mockMvc.perform(post(ProtectedPaths.CHANGE_PASSWORD).with(user(TEST_USER))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(requestTo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(NMRConstants.SUCCESS_RESPONSE));
    }
}