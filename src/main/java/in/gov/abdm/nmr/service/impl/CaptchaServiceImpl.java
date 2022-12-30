package in.gov.abdm.nmr.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.imageio.ImageIO;

import in.gov.abdm.nmr.dto.GenerateCaptchaResponseTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaRequestTO;
import in.gov.abdm.nmr.dto.ValidateCaptchaResponseTO;
import in.gov.abdm.nmr.service.ICaptchaService;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.entity.Captcha;
import in.gov.abdm.nmr.service.ICaptchaDaoService;

@Service
public class CaptchaServiceImpl implements ICaptchaService {

    private ICaptchaDaoService captchaDaoService;

    public CaptchaServiceImpl(ICaptchaDaoService captchaDaoService) {
        this.captchaDaoService = captchaDaoService;
    }

    @Override
    public GenerateCaptchaResponseTO generateCaptcha() throws NoSuchAlgorithmException, IOException {
        Captcha captcha = captchaDaoService.generateCaptcha();
        BufferedImage bufferedImage = new BufferedImage(1200, 600, BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.getGraphics();
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

        g.setColor(Color.BLACK);
        g.setFont(new Font("TimesRoman", Font.BOLD, 250));
        g.drawString(captcha.getNum1() + " " + captcha.getOperation() + " " + captcha.getNum2() + " =", 0, 400);

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
