package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.entity.FacilityType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FacilityTypeDtoMapper {

    List<FacilityTypeTO> facilityTypeDataToDto(List<FacilityType> facilityType);


}
