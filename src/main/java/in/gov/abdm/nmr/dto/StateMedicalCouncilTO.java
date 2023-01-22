package in.gov.abdm.nmr.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;

@Data
@Builder
public class StateMedicalCouncilTO {
    private BigInteger id;
    private String code;
    private String name;
}
