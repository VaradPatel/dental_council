package in.gov.abdm.nmr.dto;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class ApplicationDetailResponseTo {
    private String requestId;
    private BigInteger applicationType;
    private String submissionDate;
    private Long pendency;
    private BigInteger currentStatus;
    private BigInteger currentGroupId;
    private List<ApplicationDetailsTo> applicationDetails;
}
