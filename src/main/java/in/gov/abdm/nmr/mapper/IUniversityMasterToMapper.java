package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.UniversityMasterTo;
import in.gov.abdm.nmr.entity.UniversityMaster;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface IUniversityMasterToMapper {
    List<UniversityMasterTo> universitiesTo(List<UniversityMaster> universitites);
}
