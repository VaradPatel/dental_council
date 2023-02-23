package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.service.ICaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@RestController
public class CaptchaController {

    private ICaptchaService captchaService;

    public CaptchaController(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @GetMapping("/generate-captcha")
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        return captchaService.generateCaptcha();
    }

    @PostMapping("/verify-captcha")
    public ValidateCaptchaResponseTO validateCaptcha(@RequestBody ValidateCaptchaRequestTO validateCaptchaRequestTO) {
        return captchaService.validateCaptcha(validateCaptchaRequestTO);
    }
    
    @GetMapping("/captcha-enabled")
    public Boolean getCaptchaEnabledFlag() {
        return captchaService.getCaptchaEnabledFlag();
    }
 }
