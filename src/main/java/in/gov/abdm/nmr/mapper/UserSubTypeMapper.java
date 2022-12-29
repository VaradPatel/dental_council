package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserSubTypeTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.entity.UserSubType;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserSubTypeMapper {

    UserSubTypeTO userSubTypeToDto(UserSubType userSubType);
}
