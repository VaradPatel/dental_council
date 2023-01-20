package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilityTO {
    @JsonProperty("facilityId")
    private String facilityId;
    @JsonProperty("facilityName")
    private String facilityName;
    @JsonProperty("ownership")
    private String ownership;
    @JsonProperty("ownershipCode")
    private String ownershipCode;
    @JsonProperty("stateName")
    private String stateName;
    @JsonProperty("stateLGDCode")
    private String stateLGDCode;
    @JsonProperty("districtName")
    private String districtName;
    @JsonProperty("subDistrictName")
    private String subDistrictName;
    @JsonProperty("villageCityTownName")
    private String villageCityTownName;
    @JsonProperty("districtLGDCode")
    private String districtLGDCode;
    @JsonProperty("subDistrictLGDCode")
    private String subDistrictLGDCode;
    @JsonProperty("villageCityTownLGDCode")
    private String villageCityTownLGDCode;
    @JsonProperty("address")
    private String address;
    @JsonProperty("pincode")
    private String pincode;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("systemOfMedicineCode")
    private String systemOfMedicineCode;
    @JsonProperty("systemOfMedicine")
    private String systemOfMedicine;
    @JsonProperty("facilityTypeCode")
    private String facilityTypeCode;
    @JsonProperty("facilityStatus")
    private String facilityStatus;
    @JsonProperty("facilityType")
    private String facilityType;
}
