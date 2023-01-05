package in.gov.abdm.nmr.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Request TO for change password
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequestTo {

    @JsonProperty("username")
    @NotBlank(message = NMRConstants.USERNAME_NOT_NULL)
    String username;

    @JsonProperty("oldPassword")
    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    String oldPassword;

    @JsonProperty("newPassword")
    @NotBlank(message = NMRConstants.PASSWORD_NOT_NULL)
    String newPassword;
}