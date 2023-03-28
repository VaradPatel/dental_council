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
public class CollegeMasterTOV2 {

    private BigInteger id;
    
    @NotBlank
    private String name;
    
    @NotNull
    private BigInteger stateId;
    private BigInteger courseId;
    
    @NotNull
    private String collegeCode;
    private String website;
    
    @NotBlank
    private String addressLine1;
    private String addressLine2;
    
    @NotNull
    private BigInteger districtId;
    private BigInteger villageId;
    
    @NotBlank
    private String pinCode;
    
    @NotNull
    private BigInteger stateMedicalCouncilId;
    
    @NotNull
    private BigInteger universityId;
    
    @NotBlank
    private String emailId;
    
    @NotBlank
    private String mobileNumber;
}
