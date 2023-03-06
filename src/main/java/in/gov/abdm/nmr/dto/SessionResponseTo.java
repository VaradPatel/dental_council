package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SessionResponseTo {

    @JsonProperty("accessToken")
    String accessToken;

    @JsonProperty("expiresIn")
    int expiresIn;
    @JsonProperty("refreshExpiresIn")
    int refreshExpiresIn;
    @JsonProperty("refreshToken")
    String refreshToken;
    @JsonProperty("tokenType")
    String tokenType;
}
