package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.dto.college.CollegeTO;
import in.gov.abdm.nmr.entity.College;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import java.util.List;


@Mapper(componentModel = ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ICollegeDtoMapper {

    List<CollegeTO> collegeDataToDto(List<College> college);

    @Mapping(target = "universityMaster", ignore = true)
    @Mapping(target = "stateMedicalCouncil", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    College collegeRegistartionDtoToEntity(CollegeRegistrationRequestTo collegeRegistrationRequestTo);
}
