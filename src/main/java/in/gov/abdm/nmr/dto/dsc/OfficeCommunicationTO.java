package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfficeCommunicationTO {
	@JsonProperty("address")
	private String address;
	
	@JsonProperty("country")
	private String country;
	
	@JsonProperty("stateUT")
	private String stateUT;
	
	@JsonProperty("district")
	private String district;
	
	@JsonProperty("city")
	private String city;
	
	@JsonProperty("subDistrict")
	private String subDistrict;
	
	@JsonProperty("pincode")
	private String pinCode;
}