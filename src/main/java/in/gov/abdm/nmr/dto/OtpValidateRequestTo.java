package in.gov.abdm.nmr.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidateRequestTo{

    @NotBlank
    String transactionId;
    
    @NotEmpty(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotEmpty(message = NMRConstants.TYPE_NOT_NULL)
    String type;

    @NotEmpty(message = NMRConstants.OTP_NOT_NULL)
    String otp;
}
