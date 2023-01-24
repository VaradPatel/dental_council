package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.WorkProfileMaster;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMasterMapper {

	HpProfileMaster hpProfileToHpProfileMaster(HpProfile hpProfile);
	
	RegistrationDetailsMaster registrationDetailsToRegistrationDetailsMaster(RegistrationDetails registrationDetails);
	
	WorkProfileMaster workProfileToWorkProfileMaster(WorkProfile workProfile);

}
