package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.NmrHprLinkage;
import in.gov.abdm.nmr.entity.NmrHprLinkageMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = ComponentModel.SPRING)
public interface INmrHprLinkageMasterMapper {
	INmrHprLinkageMasterMapper HPR_LINKAGE_MASTER_MAPPER = Mappers.getMapper(INmrHprLinkageMasterMapper.class);
	NmrHprLinkageMaster nmrHprLinkageToNmrHprLinkageMaster(NmrHprLinkage nmrHprLinkage);

}
