package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycAddressTO {

	@JsonProperty("address")
	private String address;

}