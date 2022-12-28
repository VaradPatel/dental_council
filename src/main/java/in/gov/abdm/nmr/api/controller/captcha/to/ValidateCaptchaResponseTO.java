package in.gov.abdm.nmr.api.controller.captcha.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateCaptchaResponseTO {

    private boolean validity;
}
