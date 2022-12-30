package in.gov.abdm.nmr.dto;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Request TO for reset password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordRequestTo {

    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    String username;

    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    String password;
}