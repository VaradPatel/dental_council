package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetailTO {

	@NotBlank
	@JsonProperty("fullName")
	private String fullName;
	
	@JsonProperty("fatherName")
	private String fatherName;

	@JsonProperty("motherName")
	private String motherName;

	@JsonProperty("spouseName")
	private String spouseName;

	@NotBlank
	@JsonProperty("gender")
	private String gender;

	@NotBlank
	@JsonProperty("dob")
	private String dob;

	@NotBlank
	@JsonProperty("nationality")
	private String nationality;

	@NotBlank
	@JsonProperty("mobileNumber")
	private String mobileNumber;
	
	@JsonProperty("emailId")
	private String emailId;
}