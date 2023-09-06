package in.gov.abdm.nmr.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import in.gov.abdm.nmr.security.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.service.ICaptchaService;

@RestController
public class CaptchaController {

    private ICaptchaService captchaService;
    
    private Validator validator;

    public CaptchaController(ICaptchaService captchaService, Validator validator) {
        this.captchaService = captchaService;
        this.validator = validator;
    }

    @Autowired
    ChecksumUtil checksumUtil;

    @GetMapping("/generate-captcha")
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        checksumUtil.validateChecksum();
        return captchaService.generateCaptcha();
    }

    @PostMapping("/verify-captcha")
    public ValidateCaptchaResponseTO validateCaptcha(@RequestBody ValidateCaptchaRequestTO validateCaptchaRequestTO) {
        checksumUtil.validateChecksum();
        if (captchaService.isCaptchaEnabled()) {
            Set<ConstraintViolation<ValidateCaptchaRequestTO>> constraintViolations = validator.validate(validateCaptchaRequestTO);
            if (!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
        return captchaService.verifyCaptcha(validateCaptchaRequestTO);
    }
    
    @GetMapping("/captcha-enabled")
    public boolean isCaptchaEnabled() {
        return captchaService.isCaptchaEnabled();
    }
 }
