package in.gov.abdm.nmr.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;

public interface ICaptchaService {

    GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException;

    ValidateCaptchaResponseTO validateCaptcha(ValidateCaptchaRequestTO validateCaptchaRequestTO);

    Boolean getCaptchaEnabledFlag();
}
