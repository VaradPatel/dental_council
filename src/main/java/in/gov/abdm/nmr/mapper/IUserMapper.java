package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserTO;
import in.gov.abdm.nmr.entity.User;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeMapper.class, UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IUserMapper {

    UserTO userToDto(User user);
}
