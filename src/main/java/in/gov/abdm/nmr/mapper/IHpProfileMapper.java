package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.HpProfileDetailTO;
import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.HpProfileAddResponseTO;
import in.gov.abdm.nmr.dto.HpProfileDetailResponseTO;
import in.gov.abdm.nmr.dto.HpProfileUpdateResponseTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMapper {

	SmcRegistrationDetailResponseTO SmcRegistrationToDto(HpSmcDetailTO hpSmcDetailTO);

	HpProfileDetailResponseTO HpProfileDetailToDto(HpProfileDetailTO hpProfileDetailTO);

	HpProfileUpdateResponseTO HpProfileUpdateToDto(HpProfileUpdateResponseTO hpProfileUpdateResponseTO);

	HpProfileAddResponseTO HpProfileAddToDto(HpProfileAddResponseTO hpProfileAddResponseTO);

	HpProfilePictureResponseTO HpProfilePictureUploadToDto(HpProfilePictureResponseTO hpProfilePictureResponseTO);


}
