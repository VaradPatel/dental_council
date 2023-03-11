package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class UniversityMasterTo {
    private BigInteger id;
    private BigInteger universityId;
    private String name;
    private BigInteger status;
    private BigInteger visibleStatus;
    private BigInteger collegeId;
}
