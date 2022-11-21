package in.gov.abdm.nmr.domain.user_detail;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.domain.user_sub_type.UserSubTypeDtoMapper;
import in.gov.abdm.nmr.domain.user_type.UserTypeDtoMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeDtoMapper.class, UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserDetailDtoMapper {

    UserDetailTO userDetailToDto(UserDetail userDetail);
}
