package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.jpa.entity.*;
import in.gov.abdm.nmr.jpa.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMasterMapper {

	HpProfileMaster hpProfileToHpProfileMaster(HpProfile hpProfile);
	
	RegistrationDetailsMaster registrationDetailsToRegistrationDetailsMaster(RegistrationDetails registrationDetails);
	
	WorkProfileMaster workProfileToWorkProfileMaster(WorkProfile workProfile);

}
