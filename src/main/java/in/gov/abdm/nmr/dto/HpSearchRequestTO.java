package in.gov.abdm.nmr.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class HpSearchRequestTO {

    private String fullName;
    private String registrationNumber;
    private String registrationYear;
    private BigInteger stateMedicalCouncilId;
    private BigInteger profileStatusId;
}
