package in.gov.abdm.nmr.api.controller.captcha.to;

import lombok.Data;

@Data
public class ValidateCaptchaRequestTO {

    private String transactionId;
    private Integer result;
}
