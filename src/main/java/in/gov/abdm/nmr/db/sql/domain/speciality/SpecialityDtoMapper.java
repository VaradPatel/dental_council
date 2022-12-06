package in.gov.abdm.nmr.db.sql.domain.speciality;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SpecialityDtoMapper {

    List<SpecialityTO> SpecialityDataToDto(List<Speciality> speciality); 


}
