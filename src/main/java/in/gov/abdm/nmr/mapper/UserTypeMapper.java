package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserTypeTO;
import in.gov.abdm.nmr.entity.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserTypeMapper {

    UserTypeTO userTypeToDto(UserType userType);
}
