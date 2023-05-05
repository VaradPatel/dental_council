package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CollegeMasterTo;
import in.gov.abdm.nmr.entity.CollegeMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ICollegeMasterMapper {

    ICollegeMasterMapper COLLEGE_MASTER_MAPPER = Mappers.getMapper(ICollegeMasterMapper.class);

    List<CollegeMasterTo> collegeMasterTo(List<CollegeMaster> collegeMaster);
}
