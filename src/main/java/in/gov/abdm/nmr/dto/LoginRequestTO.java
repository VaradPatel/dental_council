package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestTO {

    private String username;
    private String password;
    private BigInteger userType;
    private BigInteger loginType;
    private String captchaTransId;
    private String otpTransId;
}
