package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.Captcha;

import java.security.NoSuchAlgorithmException;

public interface ICaptchaDaoService {

    Captcha generateCaptcha() throws NoSuchAlgorithmException;

    boolean validateCaptcha(String transId, Integer result);

    boolean isCaptchaValidated(String transId);

    Boolean getCaptchaEnabledFlag();
}
