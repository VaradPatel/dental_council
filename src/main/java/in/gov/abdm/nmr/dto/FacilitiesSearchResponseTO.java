package in.gov.abdm.nmr.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class FacilitiesSearchResponseTO {

    @JsonProperty("referenceNumber")
    private String referenceNumber;

    @JsonProperty("facilities")
    private List<FacilityTO> facilities;
}
