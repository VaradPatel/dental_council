package in.gov.abdm.nmr.db.sql.domain.captcha;

import java.security.NoSuchAlgorithmException;

public interface ICaptchaDaoService {

    Captcha generateCaptcha() throws NoSuchAlgorithmException;

    boolean validateCaptcha(String transId, Integer result);

    boolean isCaptchaValidated(String transId);

    Boolean getCaptchaEnabledFlag();
}
