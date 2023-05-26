package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PractitionerPersonalInformationTO {
    @JsonProperty("salutation")
    private String salutation;
    @JsonProperty("firstName")
    private String firstName;
    @JsonProperty("middleName")
    private String middleName;
    @JsonProperty("lastName")
    private String lastName;
    @JsonProperty("fatherName")
    private String fatherName;
    @JsonProperty("motherName")
    private String motherName;
    @JsonProperty("spouseName")
    private String spouseName;
    @JsonProperty("nationality")
    private String nationality;
    @JsonProperty("languagesSpoken")
    private String languagesSpoken;
}
