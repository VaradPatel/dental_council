package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DscRequestTo {
	
	@JsonProperty("signingPlace")
	private String signingPlace;
	
	@JsonProperty("nmrDetails")
	private DocumentDetailsTO documentDetailsTO;
}	