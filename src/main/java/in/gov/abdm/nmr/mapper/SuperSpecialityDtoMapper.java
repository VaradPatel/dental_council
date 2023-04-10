package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.entity.SuperSpeciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SuperSpecialityDtoMapper {

    List<SuperSpecialityTO> specialityDataToDto(List<SuperSpeciality> superSpeciality);


}
