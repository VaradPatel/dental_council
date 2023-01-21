package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class HpWorkProfileUpdateRequestTO {
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
    private String requestId;
}
