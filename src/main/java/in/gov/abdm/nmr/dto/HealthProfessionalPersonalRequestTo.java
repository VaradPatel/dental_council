package in.gov.abdm.nmr.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class HealthProfessionalPersonalRequestTo {
    private String mobileNumber;
    private String email;
    @NotBlank(message = "transactionId cannot be NULL or Blank")
    private String transactionId;
}
