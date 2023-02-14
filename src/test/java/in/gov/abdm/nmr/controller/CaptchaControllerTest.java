package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.service.ICaptchaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptchaControllerTest {

    @InjectMocks
    private CaptchaController captchaController;
    @Mock
    private ICaptchaService captchaService;
    GenerateCaptchaResponseTO responseTO;
    ValidateCaptchaRequestTO request;
    ValidateCaptchaResponseTO expectedResponse;

    @BeforeEach
    void setUp() {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(captchaController).build();
        responseTO = new GenerateCaptchaResponseTO();
        request = new ValidateCaptchaRequestTO();
        expectedResponse = new ValidateCaptchaResponseTO();
    }

    @AfterEach
    void tearDown() {
        responseTO = null;
        request = null;
        expectedResponse = null;
    }


    @Test
    public void testGenerateCaptcha() throws NoSuchAlgorithmException, IOException {
        GenerateCaptchaResponseTO expectedResponse = new GenerateCaptchaResponseTO();
        when(captchaService.generateCaptcha()).thenReturn(expectedResponse);
        GenerateCaptchaResponseTO actualResponse = captchaController.generateCaptcha();
        assertEquals(expectedResponse, actualResponse);
    }

/*    @Test
    public void generateCaptchaTest() throws Exception {
        when(captchaService.generateCaptcha()).thenReturn(responseTO);
        mockMvc.perform(MockMvcRequestBuilders.get("/generateCaptcha")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isOk());
    }*/

    @Test
    void testValidateCaptcha() {
        when(captchaService.validateCaptcha(request)).thenReturn(expectedResponse);
        ValidateCaptchaResponseTO actualResponse = captchaController.validateCaptcha(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetCaptchaEnabledFlag() {
        Boolean expectedFlag = true;
        when(captchaService.getCaptchaEnabledFlag()).thenReturn(expectedFlag);
        Boolean actualFlag = captchaController.getCaptchaEnabledFlag();
        assertEquals(expectedFlag, actualFlag);
    }
}