package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class FacilitySearchResponseTO {
    private String message;
    private List<FacilityTO> facilities;
    @JsonProperty("totalFacilities")
    private Integer totalFacilities;
    @JsonProperty("numberOfPages")
    private Integer numberOfPages;
}
