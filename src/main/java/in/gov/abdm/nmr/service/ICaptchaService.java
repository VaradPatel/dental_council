package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public interface ICaptchaService {

    GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException;

    ValidateCaptchaResponseTO verifyCaptcha(ValidateCaptchaRequestTO validateCaptchaRequestTO);

    Boolean isCaptchaEnabled();

    boolean isCaptchaVerified(String id);
}
