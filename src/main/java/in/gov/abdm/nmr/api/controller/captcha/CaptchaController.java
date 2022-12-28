package in.gov.abdm.nmr.api.controller.captcha;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.api.controller.captcha.to.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaResponseTO;

@RestController
public class CaptchaController {

    private ICaptchaService captchaService;

    public CaptchaController(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/generateCaptcha")
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        return captchaService.generateCaptcha();
    }

    @GetMapping("/validateCaptcha")
    public ValidateCaptchaResponseTO validateCaptcha(@RequestBody ValidateCaptchaRequestTO validateCaptchaRequestTO) {
        return captchaService.validateCaptcha(validateCaptchaRequestTO);
    }
    
    @GetMapping("/getCaptchaEnabledFlag")
    public Boolean getCaptchaEnabledFlag() {
        return captchaService.getCaptchaEnabledFlag();
    }
 }
