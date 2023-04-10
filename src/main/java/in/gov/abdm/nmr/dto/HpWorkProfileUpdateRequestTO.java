package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HpWorkProfileUpdateRequestTO {
    private WorkDetailsTO workDetails;
    private List<CurrentWorkDetailsTO> currentWorkDetails;
    private String requestId;
    private String registrationNo;
    private List<BigInteger> languagesKnownIds;
}
