package in.gov.abdm.nmr.dto;
import javax.validation.constraints.NotBlank;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpGenerateRequestTo{

    @NotBlank(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotBlank(message = NMRConstants.TYPE_NOT_NULL)
    String type;

}