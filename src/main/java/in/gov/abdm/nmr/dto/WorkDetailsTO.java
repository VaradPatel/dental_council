package in.gov.abdm.nmr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkDetailsTO {
    private Integer isUserCurrentlyWorking;
    private WorkStatusTO workStatus;
    private WorkNatureTO workNature;
    private String reason;
    private String remark;
}
