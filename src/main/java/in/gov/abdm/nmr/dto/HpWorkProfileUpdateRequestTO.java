package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpWorkProfileUpdateRequestTO {
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
	private String requestId;
}
