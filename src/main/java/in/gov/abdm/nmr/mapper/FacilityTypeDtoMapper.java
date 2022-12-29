package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.entity.FacilityType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FacilityTypeDtoMapper {

    List<FacilityTypeTO> FacilityTypeDataToDto(List<FacilityType> facilityType);


}
