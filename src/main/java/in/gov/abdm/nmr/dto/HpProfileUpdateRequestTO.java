package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.util.List;

@Data
public class HpProfileUpdateRequestTO {

	private PersonalDetailsTO personalDetails;
	private AddressTO communicationAddress;
	private IMRDetailsTO imrDetails;
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
    private RegistrationDetailTO registrationDetail;
    private List<QualificationDetailRequestTO> qualificationDetail;
	private String requestId;

	//eSign_change
	private String transactionId;
	private String eSignStatus;
}
