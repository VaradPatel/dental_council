package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PractitionerFacilityDataTo {
    @JsonProperty("facilityName")
    private String facilityName;
    @JsonProperty("facilityAddress")
    private String facilityAddress;
    @JsonProperty("state")
    private String state;
    @JsonProperty("district")
    private String district;
    @JsonProperty("facilityPincode")
    private String facilityPincode;

    @JsonProperty("facilityDesignation")
    private String facilityDesignation;
}
