package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.entity.SubDistrict;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface SubDistrictDtoMapper {

    SubDistrictDtoMapper SUB_DISTRICT_DTO_MAPPER = Mappers.getMapper(SubDistrictDtoMapper.class);

    List<SubDistrictTO> subDistrictDataToDto(List<SubDistrict> subDistrict);

}
