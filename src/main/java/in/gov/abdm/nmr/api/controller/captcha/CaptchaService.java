package in.gov.abdm.nmr.api.controller.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.api.controller.captcha.to.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.api.controller.captcha.to.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.db.sql.domain.captcha.Captcha;
import in.gov.abdm.nmr.db.sql.domain.captcha.ICaptchaDaoService;

@Service
public class CaptchaService implements ICaptchaService {

    private ICaptchaDaoService captchaDaoService;

    public CaptchaService(ICaptchaDaoService captchaDaoService) {
        this.captchaDaoService = captchaDaoService;
    }

    @Override
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        Captcha captcha = captchaDaoService.generateCaptcha();
        BufferedImage bufferedImage = new BufferedImage(1200, 600, BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.getGraphics();
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 25));
        g.drawString(captcha.getNum1() + " " + captcha.getOperation() + " " + captcha.getNum1() + " =", 0, 400);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", outputStream);

        return new GenerateCaptchaResponseTO(captcha.getTransactionId(), Base64.getEncoder().encodeToString(outputStream.toByteArray()));
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
