package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * response dto from aadhaar global service of send otp request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarResponseTo {

    /**
     * reason for failure
     */
    private String reason;
    /**
     * status of request of otp using aadhaar service
     */
    private String status;
    /**
     * response error code
     */
    private String responseCode;
    /**
     * error code response
     */
    private String errorCode;
    /**
     * device type
     */
    private String deviceType;
    /**
     * response from uidai
     */
    private String response;
    /**
     * request xml shared with uidai to send otp
     */
    private String requestXml;
    /**
     * error code
     */
    private String code;
    /**
     * DO auth otp object
     */
    @JsonProperty("DOAuthOTP")
    private AadhaarAuthOtpTo aadhaarAuthOtpDto;
    /**
     * user kyc details object
     */
    @JsonProperty("UserKycData")
    private AadhaarUserKycTo aadhaarUserKycDto;

}
