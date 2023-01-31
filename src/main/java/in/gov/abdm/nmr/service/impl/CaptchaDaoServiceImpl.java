package in.gov.abdm.nmr.service.impl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import in.gov.abdm.nmr.repository.ICaptchaRepository;
import in.gov.abdm.nmr.entity.Captcha;
import in.gov.abdm.nmr.service.ICaptchaDaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CaptchaDaoServiceImpl implements ICaptchaDaoService {

    private ICaptchaRepository captchaRepository;
    
    private boolean captchaEnabled;

    public CaptchaDaoServiceImpl(ICaptchaRepository captchaRepository, @Value("${nmr.captcha.enabled}") boolean captchaEnabled) {
        this.captchaRepository = captchaRepository;
        this.captchaEnabled = captchaEnabled;
    }

    private static final List<String> OPERATORS = Arrays.asList("+", "-");

    @Override
    public Captcha generateCaptcha() throws NoSuchAlgorithmException {
        Captcha captcha = new Captcha();
        SecureRandom secureRandom = SecureRandom.getInstanceStrong();


        captcha.setNum1(secureRandom.nextInt(51, 100));
        captcha.setNum2(secureRandom.nextInt(1, 50));
        captcha.setOperation(OPERATORS.get(secureRandom.nextInt(OPERATORS.size())));

        if ("+".equals(captcha.getOperation())) {
            captcha.setResult(Math.addExact(captcha.getNum1(), captcha.getNum2()));
        } else if ("-".equals(captcha.getOperation())) {
            captcha.setResult(Math.subtractExact(captcha.getNum1(), captcha.getNum2()));
        }

        captcha.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(5)));
        captcha.setExpired(false);
        captcha.setTransactionId(UUID.randomUUID().toString());

        return captchaRepository.saveAndFlush(captcha);
    }

    @Override
    public boolean validateCaptcha(String transId, Integer result) {
        Captcha captcha = captchaRepository.findByTransactionIdAndResultAndExpiredIsFalseAndExpiresAtAfter(transId, result, Timestamp.valueOf(LocalDateTime.now()));
        boolean isCaptchaValid = captcha != null;
        if (isCaptchaValid) {
            captcha.setExpired(true);
        }
        return isCaptchaValid;
    }

    @Override
    public boolean isCaptchaValidated(String transId) {
        if(!captchaEnabled) {
            return true;
        }
        return captchaRepository.findByTransactionIdAndExpiredIsTrueAndUpdatedAtBetween(transId, Timestamp.valueOf(LocalDateTime.now().minusMinutes(1)), Timestamp.valueOf(LocalDateTime.now())) != null;
    }
    
    @Override
    public Boolean getCaptchaEnabledFlag() {
        return captchaEnabled;
    }
}
