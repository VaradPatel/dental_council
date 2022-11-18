package in.gov.abdm.nmr.domain.user_type;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserTypeDtoMapper {

    UserTypeTO userTypeToDto(UserType userType);
}
