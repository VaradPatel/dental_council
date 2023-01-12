package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpSearchRequestTO {

    private String fullName;
    private String registrationNumber;
    private String registrationYear;
    private BigInteger stateMedicalCouncilId;
    private BigInteger profileStatusId;
    private int page = 0;
    private int size = 10;
}
