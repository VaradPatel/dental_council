package in.gov.abdm.nmr.db.sql.domain.user_detail.to;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.user_detail.UserDetail;
import in.gov.abdm.nmr.db.sql.domain.user_sub_type.to.UserSubTypeMapper;
import in.gov.abdm.nmr.db.sql.domain.user_type.to.UserTypeMapper;

@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeMapper.class, UserTypeMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface IUserDetailMapper {

    UserDetailTO userDetailToDto(UserDetail userDetail);
}
