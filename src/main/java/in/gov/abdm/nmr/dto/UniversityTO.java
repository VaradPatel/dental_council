package in.gov.abdm.nmr.dto;

import java.math.BigInteger;

import lombok.Data;

@Data
public class UniversityTO {

    private BigInteger id;
    private String name;
    private String nationality;
}
