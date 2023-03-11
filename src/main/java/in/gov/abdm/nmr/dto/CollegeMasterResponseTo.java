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
public class CollegeMasterResponseTo {
    private BigInteger id;
    private BigInteger collegeId;
    private String name;
    private BigInteger stateId;

}
