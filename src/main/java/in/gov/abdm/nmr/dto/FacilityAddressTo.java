package in.gov.abdm.nmr.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FacilityAddressTo {

    @JsonProperty("use")
    private String use;

    @JsonProperty("addressType")
    private String addressType;

    @JsonProperty("addressLine1")
    private String addressLine1;

    @JsonProperty("addressLine2")
    private String addressLine2;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("state")
    private String state;

    @JsonProperty("pincode")
    private String pincode;
}
