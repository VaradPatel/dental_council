package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class HealthProfessionalApplicationRequestParamsTo {
    private String smcId;
    private String registrationNo;
    private String workFlowStatusId;
    private String applicationTypeId;
    private int pageNo;
    private int size;
    private String sortBy;
    private String sortOrder;
}