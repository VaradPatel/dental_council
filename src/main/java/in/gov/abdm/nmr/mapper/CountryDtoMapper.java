package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    List<CountryTO> countryDataToDto(List<Country> country);

}
