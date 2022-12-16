package in.gov.abdm.nmr.api.controller.hp.to;

import java.util.List;

import in.gov.abdm.nmr.db.sql.domain.address.AddressTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.CurrentWorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IMRDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.PersonalDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.SpecialityDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.WorkDetailsTO;
import in.gov.abdm.nmr.db.sql.domain.qualification_detail.QualificationDetailTO;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetailTO;
import lombok.Data;

@Data
public class HpProfileDetailResponseTO {

	private PersonalDetailsTO personalDetails;
	private AddressTO communicationAddress;
	private IMRDetailsTO imrDetails;
	private SpecialityDetailsTO specialityDetails;
	private WorkDetailsTO workDetails;
	private CurrentWorkDetailsTO currentWorkDetails;
    private RegistrationDetailTO registrationDetail;
    private List<QualificationDetailTO> qualificationDetail;
}
