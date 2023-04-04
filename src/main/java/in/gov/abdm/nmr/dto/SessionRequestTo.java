package in.gov.abdm.nmr.dto;

import co.elastic.clients.elasticsearch.security.GrantType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionRequestTo {
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("clientSecret")
    private String clientSecret;
    @JsonProperty("refreshToken")
    private String refreshToken;
    @JsonProperty("grantType")
    private GrantType grantType;
}
