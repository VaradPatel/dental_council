package in.gov.abdm.nmr.entity;

import in.gov.abdm.nmr.dto.PractitionerRequestTO;
import lombok.Data;

@Data
public class HPRRequestTo {
    private String authorization;

    private PractitionerRequestTO practitionerRequestTO;
}
