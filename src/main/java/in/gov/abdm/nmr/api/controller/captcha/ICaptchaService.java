package in.gov.abdm.nmr.api.controller.captcha;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import in.gov.abdm.nmr.api.controller.captcha.to.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaResponseTO;

public interface ICaptchaService {

    GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException;

    ValidateCaptchaResponseTO validateCaptcha(ValidateCaptchaRequestTO validateCaptchaRequestTO);

    Boolean getCaptchaEnabledFlag();
}
