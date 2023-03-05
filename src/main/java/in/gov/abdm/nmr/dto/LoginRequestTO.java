package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequestTO {

    private String username;
    private String password;
    private BigInteger userType;
    private BigInteger loginType;
    private String captchaTransId;
    private String otpTransId;
}
