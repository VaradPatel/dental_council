package in.gov.abdm.nmr.db.sql.domain.registration_renewation_type;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface RegistrationRenewationTypeDtoMapper {

    List<RegistrationRenewationTypeTO> RegistrationRenewationTypeDataToDto(List<RegistrationRenewationType> registrationRenewationType); 


}
