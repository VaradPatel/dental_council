package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.entity.District;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeDtoMapper.class, UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DistrictDtoMapper {

    List<DistrictTO> districtDataToDto(List<District> state);

}
