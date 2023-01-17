package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetailTO {
	@JsonProperty("firstName")
	private String firstName;
	
	@JsonProperty("userId")
	private String userId;

	@JsonProperty("middleName")
	private String middleName;

	@JsonProperty("lastName")
	private String lastName;
	
	@JsonProperty("qualification")
	private String qualification;

	@JsonProperty("mobileNumber")
	private String mobileNumber;
	
	@JsonProperty("emailId")
	private String emailId;
}