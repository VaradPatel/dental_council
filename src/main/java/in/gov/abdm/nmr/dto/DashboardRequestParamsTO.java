package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DashboardRequestParamsTO {
    private String workFlowStatusId;
    private String applicationTypeId;
    private String name;
    private String nmrId;
    private String search;
    private String smcId;
    private String collegeId;
    private int pageNo;
    private int size;
    private String sortBy;
    private String sortOrder;
}
