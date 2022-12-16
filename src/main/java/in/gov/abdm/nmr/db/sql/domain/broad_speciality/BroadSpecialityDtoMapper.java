package in.gov.abdm.nmr.db.sql.domain.broad_speciality;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BroadSpecialityDtoMapper {

    List<BroadSpecialityTO> SpecialityDataToDto(List<BroadSpeciality> broadSpeciality); 


}
