package in.gov.abdm.nmr.api.controller.aadhaar.to;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

/**
 * Request TO for generate API
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarOtpGenerateRequestTo {

    @JsonProperty("aadhaarNumber")
    @NotBlank(message = NMRConstants.AADHAR_NOT_NULL)
    String aadhaarNumber;

}