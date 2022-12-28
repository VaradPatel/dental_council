package in.gov.abdm.nmr.api.security.controller.to;

import lombok.Data;

@Data
public class LoginRequestTO {

    private String username;
    private String password;
    private String userType;
    private String userSubType;
    private String captchaTransId;
}
