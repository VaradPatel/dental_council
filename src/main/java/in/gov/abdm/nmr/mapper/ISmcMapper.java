package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.SMCProfileTO;
import in.gov.abdm.nmr.jpa.entity.SMCProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ISmcMapper {
    SMCProfileTO smcProfileToDto(SMCProfile smcProfile);
}
