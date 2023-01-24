package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.NmrHprLinkage;
import in.gov.abdm.nmr.entity.NmrHprLinkageMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface INmrHprLinkageMasterMapper {

	NmrHprLinkageMaster nmrHprLinkageToNmrHprLinkageMaster(NmrHprLinkage nmrHprLinkage);

}
