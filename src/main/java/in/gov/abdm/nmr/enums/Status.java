package in.gov.abdm.nmr.enums;

import in.gov.abdm.nmr.util.NMRConstants;
import lombok.Getter;

@Getter
public enum Status {
    APPROVED(NMRConstants.APPROVED),
    VERIFIED(NMRConstants.VERIFIED),
    PENDING(NMRConstants.PENDING),
    SUBMITTED(NMRConstants.SUBMITTED),
    QUERY_RAISED(NMRConstants.QUERY_RAISED);

    private final String name;

    private Status(String name) {
        this.name = name;
    }
}
