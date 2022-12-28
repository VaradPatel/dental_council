package in.gov.abdm.nmr.api.controller.aadhaar.to;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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