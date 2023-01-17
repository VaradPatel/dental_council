package in.gov.abdm.nmr.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeDeanProfileTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeRegistrarProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.entity.College;
import in.gov.abdm.nmr.entity.CollegeDean;
import in.gov.abdm.nmr.entity.CollegeRegistrar;

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
