package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.jpa.entity.SubDistrict;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING, uses = {}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public interface SubDistrictDtoMapper {

    List<SubDistrictTO> subDistrictDataToDto(List<SubDistrict> subDistrict);

}
