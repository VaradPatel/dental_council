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
public class UserProfileTO {
    
    @NotNull
    private BigInteger typeId;
    
    @NotNull
    private BigInteger subTypeId;
    
    @NotBlank
    private String name;
    
    @NotBlank
    private String mobileNumber;
    
    @NotBlank
    private String emailId;
    
    private BigInteger smcId;
}
