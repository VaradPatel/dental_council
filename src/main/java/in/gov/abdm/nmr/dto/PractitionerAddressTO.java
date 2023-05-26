package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PractitionerAddressTO {
    @JsonProperty("isCommunicationAddressAsPerKYC")
    private Integer isCommunicationAddressAsPerKYC;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
    @JsonProperty("district")
    private String district;
    @JsonProperty("subDistrict")
    private String subDistrict;
    @JsonProperty("city")
    private String city;
    @JsonProperty("postalCode")
    private String postalCode;
}
