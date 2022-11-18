package in.gov.abdm.nmr.domain.password;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.domain.user_detail.UserDetailDtoMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserDetailDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordDtoMapper {

    @Mapping(target = "userDetail.password", ignore = true)
    PasswordTO passwordToDto(Password password);
}
