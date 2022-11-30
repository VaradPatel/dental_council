package in.gov.abdm.nmr.domain.city;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CityDtoMapper {

    List<CityTO> cityDataToDto(List<City> city);

}
