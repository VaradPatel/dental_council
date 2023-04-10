package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.entity.Villages;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface VillagesDtoMapper {

    List<VillagesTO> villageDataToDto(List<Villages> village);

}
