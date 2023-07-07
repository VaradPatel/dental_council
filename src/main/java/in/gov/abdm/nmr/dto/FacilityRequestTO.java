package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilityRequestTO {
    @JsonProperty("facility")
    private FacilitySearchRequestTO facility;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("timestamp")
    private String timestamp;
}
