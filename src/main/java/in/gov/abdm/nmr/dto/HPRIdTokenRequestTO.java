package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HPRIdTokenRequestTO {
    @JsonProperty("idType")
    private String idType;
    @JsonProperty("domainName")
    private String domainName;
    @JsonProperty("hprId")
    private String hprId;
}

