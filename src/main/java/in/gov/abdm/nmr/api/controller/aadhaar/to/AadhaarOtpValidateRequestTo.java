package in.gov.abdm.nmr.api.controller.aadhaar.to;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request TO for validate API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarOtpValidateRequestTo {

    @JsonProperty("aadhaarNumber")
    @NotEmpty(message = NMRConstants.AADHAR_NOT_NULL)
    String aadhaarNumber;

    @JsonProperty("txnId")
    @NotEmpty(message = NMRConstants.TRANSACTION_ID_NOT_NULL)
    String txnId;

    @JsonProperty("otp")
    @NotEmpty(message = NMRConstants.OTP_NOT_NULL)
    String otp;
}
