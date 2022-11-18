package in.gov.abdm.nmr.domain.user_sub_type;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.domain.user_type.UserTypeDtoMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserSubTypeDtoMapper {

    UserSubTypeTO userSubTypeToDto(UserSubType userSubType);
}
