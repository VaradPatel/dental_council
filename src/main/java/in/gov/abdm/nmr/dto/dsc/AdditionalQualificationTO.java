package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalQualificationTO {
    @NotBlank
    @JsonProperty("nameOfDegreeDiploma")
    private String nameOfDegreeDiploma;
    @NotBlank
    @JsonProperty("countryName")
    private String countryName;
    @NotBlank
    @JsonProperty("stateName")
    private String stateName;
    @NotBlank
    @JsonProperty("collegeName")
    private String collegeName;
    @NotBlank
    @JsonProperty("universityName")
    private String universityName;
    @NotBlank
    @JsonProperty("monthOfGraduation")
    private String monthOfGraduation;
    @NotBlank
    @JsonProperty("yearOfGraduation")
    private String yearOfGraduation;
    @NotBlank
    @JsonProperty("broadSpeciality")
    private String broadSpeciality;
    @JsonProperty("superSpeciality")
    private String superSpeciality;
    @NotBlank
    @JsonProperty("isDegreeCardAttached")
    private String isDegreeCardAttached;


}
