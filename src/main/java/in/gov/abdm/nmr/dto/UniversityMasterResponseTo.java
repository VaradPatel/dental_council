package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityMasterResponseTo {
    private BigInteger id;
    private BigInteger universityId;
    private String name;
    private BigInteger collegeId;
}