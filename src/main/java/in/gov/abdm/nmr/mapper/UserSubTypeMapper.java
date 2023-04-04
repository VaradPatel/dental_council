package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserSubTypeTO;
import in.gov.abdm.nmr.jpa.entity.UserSubType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserSubTypeMapper {

    UserSubTypeTO userSubTypeToDto(UserSubType userSubType);
}
