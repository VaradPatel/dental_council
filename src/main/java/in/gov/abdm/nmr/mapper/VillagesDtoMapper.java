package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.entity.Villages;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface VillagesDtoMapper {

    VillagesDtoMapper VILLAGES_DTO_MAPPER = Mappers.getMapper(VillagesDtoMapper.class);

    List<VillagesTO> villageDataToDto(List<Villages> village);

}
