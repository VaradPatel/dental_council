package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilityTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("transactionId")
    private String transactionId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("active")
    private String active;

    @JsonProperty("systemOfMedicine")
    private String systemOfMedicine;

    @JsonProperty("contactNumber")
    private String contactNumber;

    @JsonProperty("latitude")
    private String latitude;

    @JsonProperty("langitude")
    private String longitude;

    @JsonProperty("photo")
    private String photo;

    @JsonProperty("facilityType")
    private String facilityType;

    @JsonProperty("facilityProfileStatus")
    private String facilityProfileStatus;

    @JsonProperty("docDesignation")
    private String docDesignation;

    @JsonProperty("docDepartment")
    private String docDepartment;

    @JsonProperty("address")
    private FacilityAddressTo facilityAddressTo;
}
