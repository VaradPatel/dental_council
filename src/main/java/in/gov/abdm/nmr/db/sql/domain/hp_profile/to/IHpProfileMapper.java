package in.gov.abdm.nmr.db.sql.domain.hp_profile.to;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.api.controller.hp.to.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailResponseTO;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

@Mapper(componentModel = ComponentModel.SPRING)
//@Mapper(componentModel = ComponentModel.SPRING, uses = {StateMedicalCouncilTO.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IHpProfileMapper {

//    List<CountryTO> CountryDataToDto(List<Country> country); 
	SmcRegistrationDetailResponseTO SmcRegistrationToDto(HpSmcDetailTO hpSmcDetailTO);

	HpProfileDetailResponseTO HpProfileDetailToDto(HpProfileDetailTO hpProfileDetailTO);
	
	SmcRegistrationDetailResponseTO HpProfileUpdateToDto(HpSmcDetailTO hpSmcDetailTO);

}
