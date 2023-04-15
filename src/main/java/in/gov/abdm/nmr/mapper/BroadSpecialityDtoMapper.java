package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.entity.BroadSpeciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BroadSpecialityDtoMapper {

    BroadSpecialityDtoMapper BROAD_SPECIALITY_DTO_MAPPER = Mappers.getMapper(BroadSpecialityDtoMapper.class);

    List<BroadSpecialityTO> specialityDataToDto(List<BroadSpeciality> broadSpeciality);


}
