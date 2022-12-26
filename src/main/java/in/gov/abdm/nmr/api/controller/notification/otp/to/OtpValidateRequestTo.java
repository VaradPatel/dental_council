package in.gov.abdm.nmr.api.controller.notification.otp.to;

import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidateRequestTo{

    @NotEmpty(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotEmpty(message = NMRConstants.TYPE_NOT_NULL)
    String type;

    @NotEmpty(message = NMRConstants.OTP_NOT_NULL)
    String otp;
}
