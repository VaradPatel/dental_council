package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CollegeMasterTo;
import in.gov.abdm.nmr.entity.CollegeMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ICollegeMasterMapper {
    List<CollegeMasterTo> collegeMasterTo(List<CollegeMaster> collegeMaster);
}
