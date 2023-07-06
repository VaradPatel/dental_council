package in.gov.abdm.nmr.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FacilitySearchRequestTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("ownership")
    private String ownership;

    @JsonProperty("state")
    private String state;

    @JsonProperty("district")
    private String district;
}
