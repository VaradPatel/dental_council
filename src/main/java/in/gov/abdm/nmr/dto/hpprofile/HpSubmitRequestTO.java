package in.gov.abdm.nmr.dto.hpprofile;


import lombok.Data;
import java.math.BigInteger;

@Data
public class HpSubmitRequestTO {
    private BigInteger hpProfileId;
    private BigInteger applicationTypeId;
    private String requestId;
}

