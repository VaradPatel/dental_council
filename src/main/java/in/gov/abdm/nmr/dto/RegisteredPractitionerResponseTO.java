package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisteredPractitionerResponseTO {
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("hprId")
    private String hprId;
    @JsonProperty("message")
    private String message;
}
