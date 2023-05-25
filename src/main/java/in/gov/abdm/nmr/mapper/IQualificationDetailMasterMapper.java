package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.entity.QualificationDetailsMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = ComponentModel.SPRING)
public interface IQualificationDetailMasterMapper {

	IQualificationDetailMasterMapper QUALIFICATION_DETAIL_MASTER_MAPPER = Mappers.getMapper(IQualificationDetailMasterMapper.class);
	List<QualificationDetailsMaster> qualificationDetailsToQualificationDetailsMaster(List<QualificationDetails> qualificationDetails);

}
