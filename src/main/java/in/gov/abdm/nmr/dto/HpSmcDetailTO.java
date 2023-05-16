package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class HpSmcDetailTO {

    private String hpName;
    private String registrationNumber;
    private String councilName;
    private BigInteger hpProfileId;
    private String emailId;
    private boolean isAlreadyRegisteredInNmr;
}
