package in.gov.abdm.nmr.db.sql.domain.state;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.country.CountryDtoMapper;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictDtoMapper;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;


@Mapper(componentModel = ComponentModel.SPRING, uses = {CountryDtoMapper.class, DistrictDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StateDtoMapper {

    List<StateTO> stateDataToDto(List<State> state);
    
    List<DistrictTO> districtDataToDto(List<State> state);

}
