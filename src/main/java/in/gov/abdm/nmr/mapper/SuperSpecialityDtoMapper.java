package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.entity.SuperSpeciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SuperSpecialityDtoMapper {

    List<SuperSpecialityTO> SpecialityDataToDto(List<SuperSpeciality> superSpeciality);


}
