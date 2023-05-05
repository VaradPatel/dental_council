package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.entity.District;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface DistrictDtoMapper {

    DistrictDtoMapper DISTRICT_DTO_MAPPER = Mappers.getMapper(DistrictDtoMapper.class);

    List<DistrictTO> districtDataToDto(List<District> state);

}
