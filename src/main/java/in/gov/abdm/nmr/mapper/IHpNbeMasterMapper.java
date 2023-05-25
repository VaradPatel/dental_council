package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.HpNbeDetails;
import in.gov.abdm.nmr.entity.HpNbeDetailsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IHpNbeMasterMapper {

	IHpNbeMasterMapper HP_NBE_MASTER_MAPPER = Mappers.getMapper(IHpNbeMasterMapper.class);
	HpNbeDetailsMaster hpNbeToHpNbeMaster(HpNbeDetails hpNbeDetails);

}
