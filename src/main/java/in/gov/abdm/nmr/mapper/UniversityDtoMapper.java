package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UniversityTO;
import in.gov.abdm.nmr.entity.University;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UniversityDtoMapper {

    List<UniversityTO> universityDataToDto(List<University> universities);
}
