package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.jpa.entity.RegistrationRenewationType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RegistrationRenewationTypeDtoMapper {

    List<RegistrationRenewationTypeTO> registrationRenewationTypeDataToDto(List<RegistrationRenewationType> registrationRenewationType);


}
