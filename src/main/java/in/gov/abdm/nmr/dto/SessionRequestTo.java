package in.gov.abdm.nmr.dto;

import co.elastic.clients.elasticsearch.security.GrantType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SessionRequestTo {

    @JsonProperty("clientId")
    String clientId;
    @JsonProperty("clientSecret")
    String clientSecret;
    String refreshToken;
    GrantType grantType;
}
