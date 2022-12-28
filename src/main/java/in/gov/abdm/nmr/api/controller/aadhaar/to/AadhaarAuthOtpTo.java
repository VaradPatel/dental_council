package in.gov.abdm.nmr.api.controller.aadhaar.to;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * dto for auth otp details
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AadhaarAuthOtpTo {

    private String status;
    private String reason;
    private String errorCode;
    private String actionErrorCode;
    private String code;
    private String uidtkn;
    private String mobileNumber;
    private String email;

}
