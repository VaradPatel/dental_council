package in.gov.abdm.nmr.db.sql.domain.password.to;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.password.Password;
import in.gov.abdm.nmr.db.sql.domain.user.to.IUserMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {IUserMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PasswordMapper {

    @Mapping(target = "user.password", ignore = true)
    PasswordTO passwordToDto(Password password);
}
