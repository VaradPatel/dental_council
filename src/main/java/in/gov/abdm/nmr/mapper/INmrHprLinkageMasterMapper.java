package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.jpa.entity.NmrHprLinkage;
import in.gov.abdm.nmr.jpa.entity.NmrHprLinkageMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface INmrHprLinkageMasterMapper {

	NmrHprLinkageMaster nmrHprLinkageToNmrHprLinkageMaster(NmrHprLinkage nmrHprLinkage);

}
