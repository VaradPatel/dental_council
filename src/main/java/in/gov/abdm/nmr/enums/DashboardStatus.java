package in.gov.abdm.nmr.enums;

import in.gov.abdm.nmr.util.NMRConstants;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Enums for all possible actions in NMR
 */
public enum DashboardStatus {

    PENDING(BigInteger.valueOf(1), NMRConstants.PENDING, NMRConstants.PENDING, NMRConstants.PENDING),
    FORWARD(BigInteger.valueOf(2), NMRConstants.FORWARDED, NMRConstants.FORWARDED, NMRConstants.FORWARDED),
    QUERY_RAISE(BigInteger.valueOf(3), NMRConstants.QUERY_RAISED, NMRConstants.QUERY_RAISED, NMRConstants.QUERY_RAISED),
    APPROVED(BigInteger.valueOf(4), NMRConstants.APPROVED, NMRConstants.APPROVED, NMRConstants.APPROVED),
    REJECT(BigInteger.valueOf(5), NMRConstants.REJECTED, NMRConstants.REJECTED, NMRConstants.REJECTED),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), NMRConstants.BLACKLISTED, NMRConstants.BLACKLISTED, NMRConstants.SUBMITTED),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), NMRConstants.SUSPENDED, NMRConstants.SUSPENDED, NMRConstants.SUBMITTED),
    COLLEGE_NBE_VERIFIED(BigInteger.valueOf(8), NMRConstants.COLLEGE_NBE_VERIFIED, NMRConstants.COLLEGE_NBE_VERIFIED, NMRConstants.COLLEGE_NBE_VERIFIED);

    private final BigInteger id;
    private final String description;
    private final String status;
    private final String smcStatus;

    DashboardStatus(BigInteger id, String description, String status, String smcStatus) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.smcStatus=smcStatus;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getSmcStatus() {
        return smcStatus;
    }

    public static DashboardStatus getDashboardStatus(BigInteger id) {
        return Arrays.stream(DashboardStatus.values()).filter(action -> action.getId().equals(id)).findFirst().orElseThrow();
    }

    public static DashboardStatus getDashboardStatus(String status){
        return Arrays.stream(DashboardStatus.values()).filter(action -> action.getStatus().equals(status)).findFirst().orElseThrow();
    }
}
