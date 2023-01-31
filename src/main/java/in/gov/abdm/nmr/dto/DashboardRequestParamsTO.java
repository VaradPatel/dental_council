package in.gov.abdm.nmr.dto;

import lombok.Data;

@Data
public class DashboardRequestParamsTO {
    private String workFlowStatusId;
    private String name;
    private String nmrId;
    private String search;
    private int pageNo;
    private int size;
    private String sortBy;
    private String sortOrder;
}
