package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DscRequestTo {

    @JsonProperty("templateId")
    private String templateId;

    @NotBlank
    @JsonProperty("signingPlace")
    private String signingPlace;

    @Valid
    @JsonProperty("nmrDetails")
    private DocumentDetailsTO documentDetailsTO;

    @Valid
    @JsonProperty("additionalQualification")
    private AdditionalQualificationDocumentDetailsTO additionalQualification;
}