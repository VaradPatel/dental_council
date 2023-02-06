package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardRequestTO {
    private String workFlowStatusId;
    private String applicationTypeId;
    private String userGroupStatus;
    private String smcId;
    private String name;
    private String nmrId;
    private String search;
    private int pageNo;
    private int size;
    private String sortBy;
    private String sortOrder;
}
