package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalCommunicationTO {

	@JsonProperty("house")
	private String house;

	@JsonProperty("street")
	private String street;
	
	@JsonProperty("landmark")
	private String landmark;
	
	@JsonProperty("locality")
	private String locality;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("subDistrict")
	private String subDistrict;

	@JsonProperty("district")
	private String district;

	@NotBlank
	@JsonProperty("state")
	private String state;

	@NotBlank
	@JsonProperty("country")
	private String country;

	@NotBlank
	@JsonProperty("postalCode")
	private String postalCode;
}