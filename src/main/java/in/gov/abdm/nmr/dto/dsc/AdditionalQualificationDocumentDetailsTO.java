package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalQualificationDocumentDetailsTO {
    @NotBlank
    @JsonProperty("fullName")
    private String fullName;

    @NotBlank
    @JsonProperty("registrationNumber")
    private String registrationNumber;

    @JsonProperty("smc")
    private String stateMedicalCouncil;

    @Valid
    @JsonProperty("listOfQualifications")
    private List<AdditionalQualificationTO> listOfQualifications;


}
