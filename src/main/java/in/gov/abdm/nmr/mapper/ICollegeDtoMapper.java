package in.gov.abdm.nmr.mapper;

import java.util.List;

import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.College;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;


@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDtoMapper {

    List<CollegeTO> collegeDataToDto(List<College> college);
    
//    @Mapping(target = "state", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "stateMedicalCouncil", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    College collegeRegistartionDtoToEntity(CollegeRegistrationRequestTo collegeRegistrationRequestTo);
}
