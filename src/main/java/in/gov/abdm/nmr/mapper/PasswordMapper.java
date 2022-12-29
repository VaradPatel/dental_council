package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.PasswordTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.Password;

@Mapper(componentModel = ComponentModel.SPRING, uses = {IUserMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {

    @Mapping(target = "user.password", ignore = true)
    PasswordTO passwordToDto(Password password);
}
