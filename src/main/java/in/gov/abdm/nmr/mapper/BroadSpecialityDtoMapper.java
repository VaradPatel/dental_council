package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.entity.BroadSpeciality;
import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BroadSpecialityDtoMapper {

    List<BroadSpecialityTO> SpecialityDataToDto(List<BroadSpeciality> broadSpeciality);


}
