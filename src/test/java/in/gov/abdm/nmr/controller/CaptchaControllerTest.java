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
    GenerateCaptchaResponseTO generateCaptchaResponseTO;

    @BeforeEach
    void setUp() {
        responseTO = new GenerateCaptchaResponseTO();
        request = new ValidateCaptchaRequestTO();
        expectedResponse = new ValidateCaptchaResponseTO();
        generateCaptchaResponseTO = new GenerateCaptchaResponseTO();
    }

    @AfterEach
    void tearDown() {
        responseTO = null;
        request = null;
        expectedResponse = null;
    }


    @Test
    void testGenerateCaptcha() throws NoSuchAlgorithmException, IOException {
        when(captchaService.generateCaptcha()).thenReturn(generateCaptchaResponseTO);
        GenerateCaptchaResponseTO actualResponse = captchaController.generateCaptcha();
        assertEquals(generateCaptchaResponseTO, actualResponse);
    }

    @Test
    void testValidateCaptcha() {
        when(captchaService.verifyCaptcha(request)).thenReturn(expectedResponse);
        ValidateCaptchaResponseTO actualResponse = captchaController.validateCaptcha(request);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetCaptchaEnabledFlag() {
        Boolean expectedFlag = true;
        when(captchaService.isCaptchaEnabled()).thenReturn(expectedFlag);
        Boolean actualFlag = captchaController.isCaptchaEnabled();
        assertEquals(expectedFlag, actualFlag);
    }
}