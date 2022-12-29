package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserTypeTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.UserType;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserTypeMapper {

    UserTypeTO userTypeToDto(UserType userType);
}
