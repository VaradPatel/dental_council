package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.util.List;

@Data
public class FacilitySearchResponseTO {
    private String message;
    private List<FacilityTO> facilities;
    private Integer totalFacilities;
    private Integer numberOfPages;
}
