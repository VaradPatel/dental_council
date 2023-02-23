package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.entity.RegistrationRenewationType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RegistrationRenewationTypeDtoMapper {

    List<RegistrationRenewationTypeTO> RegistrationRenewationTypeDataToDto(List<RegistrationRenewationType> registrationRenewationType);


}
