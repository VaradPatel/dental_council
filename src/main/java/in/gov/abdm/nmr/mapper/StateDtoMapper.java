package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.State;
import in.gov.abdm.nmr.dto.StateTO;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.DistrictTO;


@Mapper(componentModel = ComponentModel.SPRING, uses = {CountryDtoMapper.class, DistrictDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StateDtoMapper {

    List<StateTO> stateDataToDto(List<State> state);
    
    List<DistrictTO> districtDataToDto(List<State> state);

}
