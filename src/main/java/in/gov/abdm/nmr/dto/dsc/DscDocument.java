package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DscDocument {

    @JsonProperty("integratorName")
    private String integratorName;

    @JsonProperty("templateId")
    private String templateId;

    @JsonProperty("signingPlace")
    private String signingPlace;

    @Valid
    @JsonProperty("nmrDetails")
    DocumentDetailsTO documentDetails;

    @Valid
    @JsonProperty("additionalQualification")
    AdditionalQualificationDocumentDetailsTO additionalQualification;
}