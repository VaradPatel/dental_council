package in.gov.abdm.nmr.dto.hpprofile;

import java.util.List;

import in.gov.abdm.nmr.dto.*;
import lombok.Value;

@Value
public class HpProfileAddRequestTO {

	private PersonalDetailsTO personalDetails;
	private AddressTO communicationAddress;
	private IMRDetailsTO imrDetails;
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
    private RegistrationDetailTO registrationDetail;
    private List<QualificationDetailRequestTO> qualificationDetail;
}
