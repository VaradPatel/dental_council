package in.gov.abdm.nmr.db.sql.domain.user_sub_type.to;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.user_sub_type.UserSubType;
import in.gov.abdm.nmr.db.sql.domain.user_type.to.UserTypeMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserSubTypeMapper {

    UserSubTypeTO userSubTypeToDto(UserSubType userSubType);
}
