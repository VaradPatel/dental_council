package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class LoginRequestTO {

    private String username;
    private String password;
    private String userType;
    private String captchaTransId;
}
