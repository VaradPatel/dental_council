package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetNewPasswordTo {

    @NotBlank(message = NMRConstants.TOKEN_NOT_NULL)
    String token;

    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    String password;
}