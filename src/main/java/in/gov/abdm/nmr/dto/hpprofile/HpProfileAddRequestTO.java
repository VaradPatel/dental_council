package in.gov.abdm.nmr.dto.hpprofile;

import in.gov.abdm.nmr.dto.*;
import lombok.Value;

import java.math.BigInteger;
import java.util.List;

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
	private BigInteger applicationTypeId;
	private String requestId;

	//eSign_change
	private String transactionId;
	private String eSignStatus;
}
