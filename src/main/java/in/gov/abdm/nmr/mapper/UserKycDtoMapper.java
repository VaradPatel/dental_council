package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.dto.UserKycTo;
import in.gov.abdm.nmr.entity.Country;
import in.gov.abdm.nmr.entity.UserKyc;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UserKycDtoMapper {

    UserKyc userKycToToUserKyc(UserKycTo userKycTo);
    UserKycTo userKycToUserKycTo(UserKyc userKyc);

}
