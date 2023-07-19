package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpGenerateRequestTo{
    
    @NotBlank(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotNull(message = NMRConstants.USER_TYPE_NOT_NULL)
    BigInteger userType;

    @NotBlank(message = NMRConstants.TYPE_NOT_NULL)
    String type;

    @JsonProperty("is_registration")
    boolean isRegistration;

}