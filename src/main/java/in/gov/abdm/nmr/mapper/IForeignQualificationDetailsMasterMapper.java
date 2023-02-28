package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.ForeignQualificationDetailsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IForeignQualificationDetailsMasterMapper {

	List<ForeignQualificationDetailsMaster> qualificationToQualificationMaster(List<ForeignQualificationDetails> foreignQualificationDetails);

}
