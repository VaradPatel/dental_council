package in.gov.abdm.nmr.dto;

import java.util.List;

import lombok.Data;

@Data
public class HpProfileDetailTO {

	private PersonalDetailsTO personalDetails;
	private AddressTO communicationAddress;
	private IMRDetailsTO imrDetails;
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
    private RegistrationDetailTO registrationDetail;
    private List<QualificationDetailTO> qualificationDetail;
    private String requestId;

}
