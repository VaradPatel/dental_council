package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UserKycTo;
import in.gov.abdm.nmr.jpa.entity.UserKyc;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserKycDtoMapper {

    UserKyc userKycToToUserKyc(UserKycTo userKycTo);
    UserKycTo userKycToUserKycTo(UserKyc userKyc);

}
