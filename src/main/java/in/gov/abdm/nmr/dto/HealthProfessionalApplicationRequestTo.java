package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class HealthProfessionalApplicationRequestTo {
    private String smcId;
    private String registrationNo;
    private String workFlowStatusId;
    private String applicationTypeId;
    private int pageNo;
    private int offset;
    private String sortBy;
    private String sortOrder;
}
