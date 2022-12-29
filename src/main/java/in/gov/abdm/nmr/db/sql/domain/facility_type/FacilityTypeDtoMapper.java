package in.gov.abdm.nmr.db.sql.domain.facility_type;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface FacilityTypeDtoMapper {

    List<FacilityTypeTO> FacilityTypeDataToDto(List<FacilityType> facilityType); 


}
