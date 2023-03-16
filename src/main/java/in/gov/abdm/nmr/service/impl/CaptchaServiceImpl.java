package in.gov.abdm.nmr.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.apiclub.captcha.servlet.CaptchaServletUtil;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.redis.hash.Captcha;
import in.gov.abdm.nmr.service.ICaptchaDaoService;
import in.gov.abdm.nmr.service.ICaptchaService;

@Service
public class CaptchaServiceImpl implements ICaptchaService {
    
    private static final String OPERATOR_MINUS = "-";

    private static final String OPERATOR_PLUS = "+";
    
    private static final String OPERATOR_MULTIPLICATION = "*";

    private static final List<Color> COLORS = List.of(Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, //
            Color.PINK, Color.RED, Color.YELLOW);

    private static final int TEXT_SIZE = 350;

    private static final List<Font> FONTS = List.of(new Font(Font.DIALOG, Font.BOLD, TEXT_SIZE), new Font(Font.DIALOG_INPUT, Font.ITALIC, TEXT_SIZE), //
            new Font(Font.MONOSPACED, Font.PLAIN, TEXT_SIZE), new Font(Font.SANS_SERIF, Font.BOLD, TEXT_SIZE), new Font(Font.SERIF, Font.ITALIC, TEXT_SIZE));
    
    private static final List<String> OPERATORS = Arrays.asList(OPERATOR_PLUS, OPERATOR_MINUS, OPERATOR_MULTIPLICATION);

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
        cn.apiclub.captcha.Captcha captcha = new cn.apiclub.captcha.Captcha.Builder(1300, 575) //
                .addText(() -> captchaEntity.getNum1() + " " + captchaEntity.getOperation() + " " + captchaEntity.getNum2() + " = ?", new DefaultWordRenderer(COLORS, FONTS)) //
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
     * Returns the boolean value set for the property nmr.captcha.enabled.
     *  
     * @return Boolean
     */
    @Override
    public Boolean isCaptchaEnabled() {
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
