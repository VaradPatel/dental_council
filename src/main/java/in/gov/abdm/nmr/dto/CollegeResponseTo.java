package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollegeResponseTo {

    private BigInteger id;
    
    @NotBlank
    private String name;
    
    private StateTO stateTO;
    private CourseTO courseTO;
    
    @NotNull
    private String collegeCode;
    private String website;
    
    @NotBlank
    private String addressLine1;
    private String addressLine2;
    
   private DistrictTO districtTO;
    private VillagesTO villagesTO;
    
    @NotBlank
    private String pinCode;
    
    private StateMedicalCouncilTO stateMedicalCouncilTO;
    
    private UniversityTO universityTO;
    
    @NotBlank
    private String emailId;
    
    @NotBlank
    private String mobileNumber;
}
