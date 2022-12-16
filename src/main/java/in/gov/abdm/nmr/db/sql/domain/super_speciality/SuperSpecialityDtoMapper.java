package in.gov.abdm.nmr.db.sql.domain.super_speciality;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SuperSpecialityDtoMapper {

    List<SuperSpecialityTO> SpecialityDataToDto(List<SuperSpeciality> superSpeciality); 


}
