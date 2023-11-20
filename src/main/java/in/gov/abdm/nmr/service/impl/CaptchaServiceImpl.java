package in.gov.abdm.nmr.service.impl;

import cn.apiclub.captcha.servlet.CaptchaServletUtil;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.redis.hash.Captcha;
import in.gov.abdm.nmr.service.ICaptchaDaoService;
import in.gov.abdm.nmr.service.ICaptchaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
public class CaptchaServiceImpl implements ICaptchaService {
    
    private ICaptchaDaoService captchaDaoService;
    
    private boolean captchaEnabled;

    public CaptchaServiceImpl(ICaptchaDaoService captchaDaoService, @Value("${nmr.captcha.enabled}") boolean captchaEnabled) {
        this.captchaDaoService = captchaDaoService;
        this.captchaEnabled = captchaEnabled;
    }

    /**
     * Generates a random math captcha in the format num1 (operator i.e "+" or "-") num2 = ?.<br>
     * eg:- 55 + 16 = ?. <br>
     * 
     * The num1 range is between 51 & 98. <br>
     * 
     * In case of "+" operation the num2 range is between 1 & (100 - num1), so that sum does not exceed 100. <br>
     * 
     * In case of "-" operation the num range is between 1 & 50, so that the subtraction does not go negative.
     *
     * @return GenerateCaptchaResponseTO
     */
    @Override
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        Captcha captchaEntity = createCaptchaEntity();
        cn.apiclub.captcha.Captcha captcha = new cn.apiclub.captcha.Captcha.Builder(1300, 575)
                .addText(() -> captchaEntity.getNum1() + " " + captchaEntity.getOperation() + " " + captchaEntity.getNum2() + " = ?",
                        new DefaultWordRenderer(List.of(Color.BLACK), List.of(new Font(Font.DIALOG, Font.BOLD, 350))))
                .build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CaptchaServletUtil.writeImage(outputStream, captcha.getImage());
        return new GenerateCaptchaResponseTO(captchaEntity.getId(), Base64.getEncoder().encodeToString(outputStream.toByteArray()));
    }

    private Captcha createCaptchaEntity() throws NoSuchAlgorithmException {
        Captcha captchaEntity = new Captcha();

        SecureRandom secureRandom = SecureRandom.getInstanceStrong();
        captchaEntity.setNum1(secureRandom.nextInt(6, 10));
        captchaEntity.setNum2(secureRandom.nextInt(1, 5));
        captchaEntity.setOperation(OPERATORS.get(secureRandom.nextInt(OPERATORS.size())));

        if (OPERATOR_PLUS.equals(captchaEntity.getOperation())) {
            captchaEntity.setResult(Math.addExact(captchaEntity.getNum1(), captchaEntity.getNum2()));
        } else if (OPERATOR_MINUS.equals(captchaEntity.getOperation())) {
            captchaEntity.setResult(Math.subtractExact(captchaEntity.getNum1(), captchaEntity.getNum2()));
        } else if (OPERATOR_MULTIPLICATION.equals(captchaEntity.getOperation())) {
            captchaEntity.setResult(Math.multiplyExact(captchaEntity.getNum1(), captchaEntity.getNum2()));
        }

        captchaEntity.setTimeToLive(5);
        captchaEntity.setExpired(false);
        captchaEntity.setId(UUID.randomUUID().toString());
        return captchaDaoService.save(captchaEntity);
    }

    /**
     * Verifies the captcha result associated with the given transactionId.
     *  
     * @return ValidateCaptchaResponseTO
     */
    @Override
    public ValidateCaptchaResponseTO verifyCaptcha(ValidateCaptchaRequestTO validateCaptchaRequestTO) {
        if(!captchaEnabled) {
            return new ValidateCaptchaResponseTO(true);
        }
        
        String transactionId = validateCaptchaRequestTO.getTransactionId();
        Captcha captcha = captchaDaoService.findById(transactionId);
        
        if (captcha == null || captcha.getExpired()) {
            return new ValidateCaptchaResponseTO(false);
        }
        if (captcha.getResult().equals(validateCaptchaRequestTO.getResult())) {
            captcha.setExpired(true);
            captcha.setTimeToLive(2);
            captchaDaoService.save(captcha);
            return new ValidateCaptchaResponseTO(true);
        }
        captchaDaoService.deleteById(transactionId);
        return new ValidateCaptchaResponseTO(false);
    }

    /**
     * Returns the booleanvalue set for the property nmr.captcha.enabled.
     *  
     * @return Boolean
     */
    @Override
    public boolean isCaptchaEnabled() {
        return captchaEnabled;
    }

    /**
     * Returns whether the captcha associated with the given transactionId is verified.
     *  
     * @return boolean
     */
    @Override
    public boolean isCaptchaVerified(String id) {
        if (!captchaEnabled) {
            return true;
        }
        Captcha captcha = captchaDaoService.findById(id);
        if (captcha == null) {
            return false;
        }

        captchaDaoService.deleteById(id);
        return captcha.getExpired();
    }
}
