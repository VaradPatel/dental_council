package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.entity.Villages;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

//@Mapper(componentModel = ComponentModel.SPRING, uses = {UserTypeDtoMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VillagesDtoMapper {

    List<VillagesTO> villageDataToDto(List<Villages> village);

}
