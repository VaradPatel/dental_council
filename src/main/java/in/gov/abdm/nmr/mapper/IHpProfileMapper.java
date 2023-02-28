package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Address;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.LanguagesKnown;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMapper {

	SmcRegistrationDetailResponseTO SmcRegistrationToDto(HpSmcDetailTO hpSmcDetailTO);

	HpProfileDetailResponseTO HpProfileDetailToDto(HpProfileDetailTO hpProfileDetailTO);

	HpProfileUpdateResponseTO HpProfileUpdateToDto(HpProfileUpdateResponseTO hpProfileUpdateResponseTO);

	HpProfileAddResponseTO HpProfileAddToDto(HpProfileAddResponseTO hpProfileAddResponseTO);

	HpProfilePictureResponseTO HpProfilePictureUploadToDto(HpProfilePictureResponseTO hpProfilePictureResponseTO);

	HpProfilePersonalResponseTO convertEntitiesToPersonalResponseTo(HpProfile hpProfile, Address address, List<LanguagesKnown> languages);


}
