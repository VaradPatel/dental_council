package in.gov.abdm.nmr.db.sql.domain.user_type.to;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.user_type.UserType;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserTypeMapper {

    UserTypeTO userTypeToDto(UserType userType);
}
