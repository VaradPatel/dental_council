package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
