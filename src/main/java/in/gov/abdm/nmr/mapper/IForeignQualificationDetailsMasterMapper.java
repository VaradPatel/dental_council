package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.ForeignQualificationDetailsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IForeignQualificationDetailsMasterMapper {

	IForeignQualificationDetailsMasterMapper FOREIGN_QUALIFICATION_DETAILS_MASTER_MAPPER = Mappers.getMapper(IForeignQualificationDetailsMasterMapper.class);
	List<ForeignQualificationDetailsMaster> qualificationToQualificationMaster(List<ForeignQualificationDetails> foreignQualificationDetails);

}
