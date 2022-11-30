package in.gov.abdm.nmr.domain.password.to;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.domain.password.Password;
import in.gov.abdm.nmr.domain.user_detail.to.UserDetailMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserDetailMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {

    @Mapping(target = "userDetail.password", ignore = true)
    PasswordTO passwordToDto(Password password);
}
