package in.gov.abdm.nmr.dto;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

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

    @NotNull(message = NMRConstants.USER_TYPE_NOT_NULL)
    BigInteger userType;

    @NotBlank(message = "transactionId cannot be NULL or Blank")
    private String transactionId;
}