package in.gov.abdm.nmr.db.sql.domain.college;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.country.CountryDtoMapper;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictDtoMapper;
import in.gov.abdm.nmr.db.sql.domain.district.DistrictTO;


@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CollegeDtoMapper {

    List<CollegeTO> collegeDataToDto(List<College> college);
    
}
