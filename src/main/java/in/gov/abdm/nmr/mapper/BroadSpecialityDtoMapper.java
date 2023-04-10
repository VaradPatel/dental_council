package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.entity.BroadSpeciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BroadSpecialityDtoMapper {

    List<BroadSpecialityTO> specialityDataToDto(List<BroadSpeciality> broadSpeciality);


}
