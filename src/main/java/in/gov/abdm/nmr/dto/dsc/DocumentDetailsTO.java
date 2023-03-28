package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDetailsTO {

	@Valid
	@JsonProperty("nmrPersonalDetail")
    PersonalDetailTO personalDetail;

	@JsonProperty("nmrKycAddressTO")
	KycAddressTO kycAddressTO;

	@Valid
	@JsonProperty("nmrPersonalCommunication")
    PersonalCommunicationTO personalCommunication;

	@Valid
	@JsonProperty("nmrRegistrationDetailsTO")
	RegistrationDetailsTO registrationDetailsTO;

	@Valid
	@JsonProperty("nmrQualificationDetailsTO")
	QualificationDetailsTO qualificationDetailsTO;

	@NotBlank
	@JsonProperty("isRegCerAttached")
	private String isRegCerAttached;

	@NotBlank
	@JsonProperty("isDegreeCardAttached")
	private String isDegreeCardAttached;
}