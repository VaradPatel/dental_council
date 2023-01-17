package in.gov.abdm.nmr.dto.dsc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDetailsTO {

	@JsonProperty("nmrPersonalDetail")
    PersonalDetailTO personalDetail;
	
	@JsonProperty("nmrPersonalCommunication")
    PersonalCommunicationTO personalCommunication;
    
	@JsonProperty("nmrOfficeCommunication")
	OfficeCommunicationTO officeCommunication;
	
	@JsonProperty("isRegCerAttached")
	private String isRegCerAttached;
	
	@JsonProperty("isDegreeCardAttached")
	private String isDegreeCardAttached;
	
	@JsonProperty("isOtherDocumentAttached")
	private String isOtherDocumentAttached;
}