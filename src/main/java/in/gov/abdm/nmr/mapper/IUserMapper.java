package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.User;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeMapper.class, UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IUserMapper {

    UserTO userToDto(User user);
}
