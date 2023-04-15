package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    CountryDtoMapper COUNTRY_DTO_MAPPER = Mappers.getMapper(CountryDtoMapper.class);

    List<CountryTO> countryDataToDto(List<Country> country);

    CountryTO mapToCountryTO(Country country);
}
