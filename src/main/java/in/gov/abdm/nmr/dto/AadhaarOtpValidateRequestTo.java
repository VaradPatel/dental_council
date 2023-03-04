package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.math.BigInteger;

/**
 * Request TO for validate API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarOtpValidateRequestTo {

    @JsonProperty("hpProfileId")
    BigInteger hpProfileId;

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
