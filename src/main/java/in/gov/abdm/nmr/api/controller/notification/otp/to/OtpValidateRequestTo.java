package in.gov.abdm.nmr.api.controller.notification.otp.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpValidateRequestTo{

    @NotEmpty(message = "Contact cannot be null or empty")
    String contact;

    @NotEmpty(message = "Type cannot be null or empty")
    String type;

    @NotEmpty(message = "OTP cannot be null or empty")
    String otp;
}
