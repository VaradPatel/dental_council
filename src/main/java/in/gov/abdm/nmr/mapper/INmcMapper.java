package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.NmcProfileTO;
import in.gov.abdm.nmr.entity.NmcProfile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface INmcMapper {
    NmcProfileTO nmcProfileToDto(NmcProfile nmcProfile);
}
