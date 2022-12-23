package in.gov.abdm.nmr.api.controller.notification.otp.to;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpGenerateRequestTo{

    @NotBlank(message = "Contact cannot be null or empty")
    String contact;

    @NotBlank(message = "Type cannot be null or empty")
    String type;

}