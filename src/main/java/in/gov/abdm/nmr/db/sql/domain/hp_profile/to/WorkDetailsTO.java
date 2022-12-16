package in.gov.abdm.nmr.db.sql.domain.hp_profile.to;

import in.gov.abdm.nmr.db.sql.domain.work_nature.WorkNatureTO;
import in.gov.abdm.nmr.db.sql.domain.work_status.WorkStatusTO;
import lombok.Data;

@Data
public class WorkDetailsTO {
    private Integer isUserCurrentlyWorking;
    private WorkStatusTO workStatus;
    private WorkNatureTO workNature;
}
