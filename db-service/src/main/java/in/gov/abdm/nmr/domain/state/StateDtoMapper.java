package in.gov.abdm.nmr.domain.state;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserSubTypeDtoMapper.class, UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface StateDtoMapper {

    List<StateTO> stateDataToDto(List<State> state);
//    CountryTO CountryDataToDto(Country country);

}
