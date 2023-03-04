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

    @NotBlank(message = NMRConstants.EMAIL_NOT_NULL)
    String email;

    @NotBlank(message = NMRConstants.MOBILE_NOT_NULL)
    String mobile;

    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    String username;



}