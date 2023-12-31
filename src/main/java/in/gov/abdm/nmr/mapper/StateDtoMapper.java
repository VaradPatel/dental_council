package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.entity.State;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = ComponentModel.SPRING, uses = {CountryDtoMapper.class, DistrictDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StateDtoMapper {

    StateDtoMapper STATE_DTO_MAPPER = Mappers.getMapper(StateDtoMapper.class);

    List<StateTO> stateDataToDto(List<State> state);
    
    List<DistrictTO> districtDataToDto(List<State> state);

}
