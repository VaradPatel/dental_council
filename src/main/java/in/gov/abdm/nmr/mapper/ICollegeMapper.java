package in.gov.abdm.nmr.mapper;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ICollegeMapper {

    CollegeProfileTo collegeCreationRequestToResponse(CollegeRegistrationRequestTo collegeRegistrationRequestTo);
    
    CollegeRegistrarProfileTo collegeRegistrarRequestToResponse(CollegeRegistrarCreationRequestTo collegeRegistrarCreationRequestTo);
    
    CollegeDeanProfileTo collegeDeanRequestToResponse(CollegeDeanCreationRequestTo collegeDeanCreationRequestTo);
    
    CollegeProfileTo collegeEntityToCollegeProfile(College college);
    
    @Mapping(target = "userId", ignore = true)
    CollegeRegistrarProfileTo collegeRegistrarEntityToCollegeRegistrarProfile(CollegeRegistrar collegeRegistrar);
    
    @Mapping(target = "userId", ignore = true)
    CollegeDeanProfileTo collegeDeanEntityToCollegeDeanProfile(CollegeDean collegeDean);
}
