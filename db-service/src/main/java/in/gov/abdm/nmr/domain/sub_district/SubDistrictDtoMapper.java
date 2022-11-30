package in.gov.abdm.nmr.domain.sub_district;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubDistrictDtoMapper {

    List<SubDistrictTO> districtDataToDto(List<SubDistrict> district);

}
