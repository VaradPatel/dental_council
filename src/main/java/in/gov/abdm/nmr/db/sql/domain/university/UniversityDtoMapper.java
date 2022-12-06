package in.gov.abdm.nmr.db.sql.domain.university;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UniversityDtoMapper {

    List<UniversityTO> UniversityDataToDto(List<University> universities); 
}
