package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestTO {

    private String username;
    private String password;
    private String userType;
    private String captchaTransId;
}
