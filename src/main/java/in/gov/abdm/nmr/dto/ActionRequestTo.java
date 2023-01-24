package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
public class ActionRequestTo {
    private BigInteger hpProfileId;
    private BigInteger applicationTypeId;
    private Timestamp fromDate;
    private Timestamp toDate;
    private String remarks;
}
