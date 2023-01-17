package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetSetPasswordLinkTo {

    @NotBlank(message = NMRConstants.CONTACT_NOT_NULL)
    String contact;

    @NotBlank(message = NMRConstants.TYPE_NOT_NULL)
    String type;

}