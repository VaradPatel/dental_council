package in.gov.abdm.nmr.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import cn.apiclub.captcha.servlet.CaptchaServletUtil;
import cn.apiclub.captcha.text.renderer.DefaultWordRenderer;
import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.entity.Captcha;
import in.gov.abdm.nmr.service.ICaptchaDaoService;
import in.gov.abdm.nmr.service.ICaptchaService;

@Service
public class CaptchaServiceImpl implements ICaptchaService {
    
    private static final List<Color> COLORS = List.of(Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY, Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, //
            Color.PINK, Color.RED, Color.YELLOW);

    private static final int TEXT_SIZE = 350;

    private static final List<Font> FONTS = List.of(new Font(Font.DIALOG, Font.BOLD, TEXT_SIZE), new Font(Font.DIALOG_INPUT, Font.ITALIC, TEXT_SIZE), //
            new Font(Font.MONOSPACED, Font.PLAIN, TEXT_SIZE), new Font(Font.SANS_SERIF, Font.BOLD, TEXT_SIZE), new Font(Font.SERIF, Font.ITALIC, TEXT_SIZE));

    private ICaptchaDaoService captchaDaoService;

    public CaptchaServiceImpl(ICaptchaDaoService captchaDaoService) {
        this.captchaDaoService = captchaDaoService;
    }

    @Override
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        Captcha captchaEntity = captchaDaoService.generateCaptcha();

        cn.apiclub.captcha.Captcha captcha = new cn.apiclub.captcha.Captcha.Builder(1300, 400) //
                .addText(() -> captchaEntity.getNum1() + " " + captchaEntity.getOperation() + " " + captchaEntity.getNum2() + " = ?", new DefaultWordRenderer(COLORS, FONTS)) //
                .addNoise().build();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CaptchaServletUtil.writeImage(outputStream, captcha.getImage());
        return new GenerateCaptchaResponseTO(captchaEntity.getTransactionId(), Base64.getEncoder().encodeToString(outputStream.toByteArray()));
    }

    @Override
    public ValidateCaptchaResponseTO validateCaptcha(ValidateCaptchaRequestTO validateCaptchaRequestTO) {
        return new ValidateCaptchaResponseTO(captchaDaoService.validateCaptcha(validateCaptchaRequestTO.getTransactionId(), validateCaptchaRequestTO.getResult()));
    }

    @Override
    public Boolean getCaptchaEnabledFlag() {
        return captchaDaoService.getCaptchaEnabledFlag();
    }
}
