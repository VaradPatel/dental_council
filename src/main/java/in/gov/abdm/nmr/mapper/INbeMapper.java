package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.NbeProfileTO;
import in.gov.abdm.nmr.entity.NbeProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface INbeMapper {
    NbeProfileTO nbeProfileToDto(NbeProfile nbeProfile);
}
