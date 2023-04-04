package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.jpa.entity.WorkProfile;
import in.gov.abdm.nmr.jpa.entity.WorkProfileMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IWorkProfileMasterMapper {

	WorkProfileMaster workProfileToWorkProfileMaster(WorkProfile workProfile);

}
