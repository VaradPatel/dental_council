package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.entity.FacilityType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FacilityTypeDtoMapper {
    FacilityTypeDtoMapper FACILITY_TYPE_DTO_MAPPER = Mappers.getMapper(FacilityTypeDtoMapper.class);
    List<FacilityTypeTO> facilityTypeDataToDto(List<FacilityType> facilityType);


}
