package in.gov.abdm.nmr.api.controller.notification.otp.to;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpGenerateRequestTo{

    @NotBlank(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotBlank(message = NMRConstants.TYPE_NOT_NULL)
    String type;

}