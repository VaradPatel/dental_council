package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.jpa.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    List<CountryTO> countryDataToDto(List<Country> country);

    CountryTO mapToCountryTO(Country country);
}
