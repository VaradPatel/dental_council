package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
public class DashboardRequestParamsTO {
    private String workFlowStatusId;
    private String applicationTypeId;
    private String userGroupStatus;
    private BigInteger userGroupId;
    private String smcId;
    private String name;
    private String nmrId;
    private String search;
    private String councilId;
    private String collegeId;
    private int pageNo;
    private int size;
    private String sortBy;
    private String sortOrder;
}
