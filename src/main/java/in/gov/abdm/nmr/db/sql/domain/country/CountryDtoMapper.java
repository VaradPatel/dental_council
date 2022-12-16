package in.gov.abdm.nmr.db.sql.domain.country;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    List<CountryTO> CountryDataToDto(List<Country> country); 

}
