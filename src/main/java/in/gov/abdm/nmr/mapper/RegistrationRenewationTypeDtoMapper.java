package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.entity.RegistrationRenewationType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RegistrationRenewationTypeDtoMapper {

    RegistrationRenewationTypeDtoMapper REGISTRATION_RENEWATION_TYPE_DTO_MAPPER = Mappers.getMapper(RegistrationRenewationTypeDtoMapper.class);

    List<RegistrationRenewationTypeTO> registrationRenewationTypeDataToDto(List<RegistrationRenewationType> registrationRenewationType);


}
