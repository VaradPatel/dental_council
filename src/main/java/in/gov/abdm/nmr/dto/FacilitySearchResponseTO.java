package in.gov.abdm.nmr.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilitySearchResponseTO {

    @JsonProperty("referenceNumber")
    private String referenceNumber;

    @JsonProperty("facility")
    private FacilityTO facility;
}
