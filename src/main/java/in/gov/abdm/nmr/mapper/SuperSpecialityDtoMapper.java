package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.entity.SuperSpeciality;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface SuperSpecialityDtoMapper {

    SuperSpecialityDtoMapper SUPER_SPECIALITY_DTO_MAPPER = Mappers.getMapper(SuperSpecialityDtoMapper.class);
    List<SuperSpecialityTO> specialityDataToDto(List<SuperSpeciality> superSpeciality);


}
