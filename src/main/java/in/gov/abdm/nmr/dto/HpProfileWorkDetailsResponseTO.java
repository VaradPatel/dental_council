package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class HpProfileWorkDetailsResponseTO {
    private WorkDetailsTO workDetails;
    private List<CurrentWorkDetailsTO> currentWorkDetails;
    private BigInteger hpProfileId;
    private String requestId;
    private List<BigInteger> languagesKnownIds;

}
