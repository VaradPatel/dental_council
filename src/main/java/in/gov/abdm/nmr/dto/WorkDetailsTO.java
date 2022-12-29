package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.dto.WorkNatureTO;
import in.gov.abdm.nmr.dto.WorkStatusTO;
import lombok.Data;

@Data
public class WorkDetailsTO {
    private Integer isUserCurrentlyWorking;
    private WorkStatusTO workStatus;
    private WorkNatureTO workNature;
}
