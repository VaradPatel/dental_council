package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.jpa.entity.SuperSpeciality;
import in.gov.abdm.nmr.jpa.entity.SuperSpecialityMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ISuperSpecialityMasterMapper {

	List<SuperSpecialityMaster> superSpecialityToSuperSpecialityMaster(List<SuperSpeciality> superSpecialities);

}
