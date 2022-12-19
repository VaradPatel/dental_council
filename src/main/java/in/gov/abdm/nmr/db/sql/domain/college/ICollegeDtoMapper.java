package in.gov.abdm.nmr.db.sql.domain.college;

import java.util.List;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.api.controller.college.to.CollegeRegistrationRequestTo;


@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDtoMapper {

    List<CollegeTO> collegeDataToDto(List<College> college);
    
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "stateMedicalCouncil", ignore = true)
    @Mapping(target = "userDetail", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    College collegeRegistartionDtoToEntity(CollegeRegistrationRequestTo collegeRegistrationRequestTo);
}
