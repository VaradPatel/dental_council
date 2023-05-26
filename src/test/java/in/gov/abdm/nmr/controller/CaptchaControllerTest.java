package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.service.ICaptchaService;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CaptchaController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
@ContextConfiguration(classes = CaptchaController.class)
@ActiveProfiles(profiles = "local")
class CaptchaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ICaptchaService captchaService;

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    void testGenerateCaptchaShouldGenerateCaptchaWithTransactionId() throws Exception {
        GenerateCaptchaResponseTO generateCaptchaResponseTo = new GenerateCaptchaResponseTO();
        generateCaptchaResponseTo.setImage(CommonTestData.CAPTCHA_IMAGE);
        generateCaptchaResponseTo.setTransactionId(CommonTestData.TRANSACTION_ID);

        when(captchaService.generateCaptcha()).thenReturn(generateCaptchaResponseTo);

        mockMvc.perform(get("/generate-captcha").with(user("123"))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transaction_id").value(CommonTestData.TRANSACTION_ID))
                .andExpect(jsonPath("$.image").value(CommonTestData.CAPTCHA_IMAGE));

    }

    @Test
    void testVerifyCaptchaShouldReturnValidResponse() throws Exception {
        ValidateCaptchaRequestTO validateCaptchaRequestTo = new ValidateCaptchaRequestTO();
        validateCaptchaRequestTo.setTransactionId(CommonTestData.TRANSACTION_ID);
        validateCaptchaRequestTo.setResult(CommonTestData.CAPTCHA_RESULT);
        ValidateCaptchaResponseTO validateCaptchaResponseTo = new ValidateCaptchaResponseTO();
        validateCaptchaResponseTo.setValidity(true);

        when(captchaService.verifyCaptcha(any(ValidateCaptchaRequestTO.class))).thenReturn(validateCaptchaResponseTo);

        mockMvc.perform(post("/verify-captcha")
                        .with(user("123"))
                        .with(csrf()).accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsBytes(validateCaptchaRequestTo))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());

    }

}
