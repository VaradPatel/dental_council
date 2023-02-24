package in.gov.abdm.nmr.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ValidateCaptchaRequestTO {

    @NotBlank
    private String transactionId;
    
    @NotNull
    private Integer result;
}
