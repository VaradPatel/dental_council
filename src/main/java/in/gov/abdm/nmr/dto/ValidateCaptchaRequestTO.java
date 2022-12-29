package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class ValidateCaptchaRequestTO {

    private String transactionId;
    private Integer result;
}
