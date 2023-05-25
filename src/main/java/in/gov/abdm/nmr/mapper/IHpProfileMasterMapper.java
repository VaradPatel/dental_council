package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMasterMapper {

	IHpProfileMasterMapper HP_PROFILE_MASTER_MAPPER = Mappers.getMapper(IHpProfileMasterMapper.class);

	HpProfileMaster hpProfileToHpProfileMaster(HpProfile hpProfile);
	
	RegistrationDetailsMaster registrationDetailsToRegistrationDetailsMaster(RegistrationDetails registrationDetails);

}
