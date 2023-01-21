package in.gov.abdm.nmr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileAudit;
import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.entity.RegistrationDetailsAudit;
import in.gov.abdm.nmr.entity.WorkProfile;
import in.gov.abdm.nmr.entity.WorkProfileAudit;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileAuditMapper {

	HpProfileAudit hpProfileToHpProfileAudit(HpProfile hpProfile);
	
	RegistrationDetailsAudit registrationDetailsToRegistrationDetailsAudit(RegistrationDetails registrationDetails);
	
	WorkProfileAudit workProfileToWorkProfileAudit(WorkProfile workProfile);

}
