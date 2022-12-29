package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.SubDistrict;
import in.gov.abdm.nmr.dto.SubDistrictTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface SubDistrictDtoMapper {

    List<SubDistrictTO> subDistrictDataToDto(List<SubDistrict> subDistrict);

}
