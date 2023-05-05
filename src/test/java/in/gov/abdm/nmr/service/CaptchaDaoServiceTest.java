package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.redis.hash.Captcha;
import in.gov.abdm.nmr.redis.repository.ICaptchaRepository;
import in.gov.abdm.nmr.service.impl.CaptchaDaoServiceImpl;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaptchaDaoServiceTest {

    @Mock
    private ICaptchaRepository captchaRepository;

    @InjectMocks
    CaptchaDaoServiceImpl captchaDaoService;

    private Captcha captcha = new Captcha();

    @BeforeEach
    void setup(){
        populateCaptcha();
    }

    @Test
    void testSaveCaptchaSaveAndReturnCaptcha(){
        when(captchaRepository.save(any(Captcha.class))).thenReturn(captcha);
        Captcha savedCaptcha = captchaDaoService.save(captcha);
        assertEquals("123",savedCaptcha.getId());
    }

    @Test
     void testFindByIdReturnsValidCaptcha(){
        when(captchaRepository.findById(anyString())).thenReturn(Optional.of(captcha));
        Captcha result = captchaDaoService.findById("123");
        assertEquals("123", result.getId());
        assertFalse(result.getExpired());
        assertEquals(1, result.getNum1());
        assertEquals(2, result.getNum2());
        assertEquals(3, result.getResult());
        assertEquals(200, result.getTimeToLive());
    }

    @Test
    void testDeleteByIdDeleteSavedCaptcha(){
        doNothing().when(captchaRepository).deleteById(anyString());
        captchaDaoService.deleteById("123");
        Mockito.verify(captchaRepository, Mockito.times(1)).deleteById(anyString());
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
