package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilitySearchRequestTO {
    @JsonProperty("facilityId")
    private String facilityId;
    @JsonProperty("facilityName")
    private String facilityName;
    @JsonProperty("ownershipCode")
    private String ownershipCode;
    @JsonProperty("stateLGDCode")
    private String stateLgdCode;
    @JsonProperty("districtLGDCode")
    private String districtLgdCode;
    @JsonProperty("subDistrictLGDCode")
    private String subDistrictLgdCode;

    private String pincode;
    private Integer page;
    @JsonProperty("resultsPerPage")
    private Integer resultsPerPage;
}
