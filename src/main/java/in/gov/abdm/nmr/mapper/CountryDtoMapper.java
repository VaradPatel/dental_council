package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.CountryTO;
import in.gov.abdm.nmr.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    List<CountryTO> CountryDataToDto(List<Country> country);

}
