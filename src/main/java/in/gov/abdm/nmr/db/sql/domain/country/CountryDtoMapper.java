package in.gov.abdm.nmr.db.sql.domain.country;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.StateMedicalCouncil;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

@Mapper(componentModel = ComponentModel.SPRING)
public interface CountryDtoMapper {

    List<CountryTO> CountryDataToDto(List<Country> country); 


}
