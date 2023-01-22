package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UniversityTO {

    private BigInteger id;
    private String name;
    private String nationality;
}
