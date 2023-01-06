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
public class WorkFlowRequestTO {

    private String requestId;
    private BigInteger applicationTypeId;
    private BigInteger actorId;
    private BigInteger actionId;
    private BigInteger hpProfileId;


}
