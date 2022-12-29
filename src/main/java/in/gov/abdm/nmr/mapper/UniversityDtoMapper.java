package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.University;
import in.gov.abdm.nmr.dto.UniversityTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface UniversityDtoMapper {

    List<UniversityTO> UniversityDataToDto(List<University> universities);
}
