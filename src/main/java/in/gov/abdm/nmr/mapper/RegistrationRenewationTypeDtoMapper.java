package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.RegistrationRenewationType;
import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RegistrationRenewationTypeDtoMapper {

    List<RegistrationRenewationTypeTO> RegistrationRenewationTypeDataToDto(List<RegistrationRenewationType> registrationRenewationType);


}
