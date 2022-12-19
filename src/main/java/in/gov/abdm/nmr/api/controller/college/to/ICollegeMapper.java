package in.gov.abdm.nmr.api.controller.college.to;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.db.sql.domain.college.College;
import in.gov.abdm.nmr.db.sql.domain.college_dean.CollegeDean;
import in.gov.abdm.nmr.db.sql.domain.college_registrar.CollegeRegistrar;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ICollegeMapper {

    CollegeProfileTo collegeCreationRequestToResponse(CollegeRegistrationRequestTo collegeRegistrationRequestTo);
    
    CollegeRegistrarProfileTo collegeRegistrarRequestToResponse(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
    
    CollegeDeanProfileTo collegeDeanRequestToResponse(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
    
    @Mapping(target = "councilId", ignore = true)
    @Mapping(target = "stateId", ignore = true)
    @Mapping(target = "universityId", ignore = true)
    CollegeProfileTo collegeEntityToCollegeProfile(College college);
    
    @Mapping(target = "userId", ignore = true)
    CollegeRegistrarProfileTo collegeRegistrarEntityToCollegeRegistrarProfile(CollegeRegistrar collegeRegistrar);
    
    @Mapping(target = "userId", ignore = true)
    CollegeDeanProfileTo collegeDeanEntityToCollegeDeanProfile(CollegeDean collegeDean);
}
