package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.HpProfilePictureResponseTO;
import in.gov.abdm.nmr.dto.HpSmcDetailTO;
import in.gov.abdm.nmr.dto.SmcRegistrationDetailResponseTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpProfileMapper {

    SmcRegistrationDetailResponseTO smcRegistrationToDto(HpSmcDetailTO hpSmcDetailTO);

    HpProfilePictureResponseTO hpProfilePictureUploadToDto(HpProfilePictureResponseTO hpProfilePictureResponseTO);


}
