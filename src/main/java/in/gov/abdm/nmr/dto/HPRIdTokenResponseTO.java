package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigInteger;

@Data
public class HPRIdTokenResponseTO {
    @JsonProperty("hprId")
    private String hprId;
    @JsonProperty("hprIdNumber")
    private String hprIdNumber;
    @JsonProperty("token")
    private String token;
    @JsonProperty("expiresIn")
    private BigInteger expiresIn;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("refreshExpiresIn")
    private BigInteger refreshExpiresIn;

}
