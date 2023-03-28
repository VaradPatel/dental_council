package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDetailsTO {

	@NotBlank
	@JsonProperty("councilName")
	private String councilName;

	@NotBlank
	@JsonProperty("registrationNumber")
	private String registrationNumber;
	
	@JsonProperty("registrationDate")
	private String registrationDate;

	@NotBlank
	@JsonProperty("registrationType")
	private String registrationType;
	
	@JsonProperty("dueDate")
	private String dueDate;
}