package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ApplicationDetailsTo {
    private BigInteger workflowStatusId;
    private BigInteger actionId;
    private BigInteger groupId;
    private String actionDate;
    private String remarks;
}
