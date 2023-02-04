package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.HpNbeDetailsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpNbeMasterMapper {

	HpNbeDetailsMaster hpNbeToHpNbeMaster(HpNbeDetails hpNbeDetails);

}