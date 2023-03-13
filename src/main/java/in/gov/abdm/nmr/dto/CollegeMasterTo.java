package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class CollegeMasterTo {
    private BigInteger id;
    private BigInteger collegeId;
    private String name;
    private BigInteger status;
    private BigInteger visibleStatus;
    private BigInteger systemOfMedicineId;
    private BigInteger stateId;
    private BigInteger courseId;
}
