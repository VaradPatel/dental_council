package in.gov.abdm.nmr.dto;

import in.gov.abdm.nmr.dto.WorkNatureTO;
import in.gov.abdm.nmr.dto.WorkStatusTO;
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
}
