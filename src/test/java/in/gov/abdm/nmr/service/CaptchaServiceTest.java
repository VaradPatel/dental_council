package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.redis.hash.Captcha;
import in.gov.abdm.nmr.service.impl.CaptchaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptchaServiceTest {

    @Mock
    ICaptchaDaoService captchaDaoService;

    @InjectMocks
    CaptchaServiceImpl captchaService = new CaptchaServiceImpl(captchaDaoService, true);

    Captcha captcha = new Captcha();

    @BeforeEach
    void setup(){
        populateCaptcha();
    }

    @Test
    void testGenerateCaptchaShouldGenerateValidCaptcha() throws NoSuchAlgorithmException, IOException {
        when(captchaDaoService.save(any(Captcha.class))).thenReturn(captcha);
        GenerateCaptchaResponseTO generateCaptchaResponseTO = captchaService.generateCaptcha();
        assertTrue(generateCaptchaResponseTO != null);
    }

    @Test
    void testVerifyCaptchaShouldReturnFalseResponseForNullCaptcha(){
        ValidateCaptchaRequestTO validateCaptchaRequestTO = new ValidateCaptchaRequestTO();
        validateCaptchaRequestTO.setResult(2);
        validateCaptchaRequestTO.setTransactionId("123");
        when(captchaDaoService.findById(any())).thenReturn(null);
        ValidateCaptchaResponseTO validateCaptchaResponseTO = captchaService.verifyCaptcha(validateCaptchaRequestTO);
        assertFalse(validateCaptchaResponseTO.isValidity());

    }

    @Test
    void testVerifyCaptchaShouldReturnFalseResponseForExpiredCaptcha(){
        ValidateCaptchaRequestTO validateCaptchaRequestTO = new ValidateCaptchaRequestTO();
        validateCaptchaRequestTO.setResult(2);
        validateCaptchaRequestTO.setTransactionId("123");
        captcha.setExpired(true);
        when(captchaDaoService.findById(any())).thenReturn(captcha);
        ValidateCaptchaResponseTO validateCaptchaResponseTO = captchaService.verifyCaptcha(validateCaptchaRequestTO);
        assertFalse(validateCaptchaResponseTO.isValidity());

    }

    @Test
    void testVerifyCaptchaShouldReturnTrueResponseForValidCaptcha(){
        ValidateCaptchaRequestTO validateCaptchaRequestTO = new ValidateCaptchaRequestTO();
        validateCaptchaRequestTO.setResult(3);
        validateCaptchaRequestTO.setTransactionId("123");
        when(captchaDaoService.findById(any())).thenReturn(captcha);
        ValidateCaptchaResponseTO validateCaptchaResponseTO = captchaService.verifyCaptcha(validateCaptchaRequestTO);
        assertTrue(validateCaptchaResponseTO.isValidity());

    }

    void populateCaptcha(){
        captcha.setId("123");
        captcha.setExpired(false);
        captcha.setNum1(1);
        captcha.setNum2(2);
        captcha.setOperation("Add");
        captcha.setTimeToLive(200);
        captcha.setResult(3);
    }
}
