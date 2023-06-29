package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Enums for all possible actions in NMR
 */
public enum DashboardStatus {

    PENDING(BigInteger.valueOf(1), "Pending", "Pending", "Pending"),
    FORWARD(BigInteger.valueOf(2), "Forwarded", "Forwarded", "Forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Query Raised", "Query Raised", "Query Raised"),
    APPROVED(BigInteger.valueOf(4), "Approved", "Approved", "Approved"),
    REJECT(BigInteger.valueOf(5), "Rejected", "Rejected", "Rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Blacklisted", "Blacklisted", "Submitted"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Suspended", "Suspended", "Submitted"),
    COLLEGE_VERIFIED(BigInteger.valueOf(8), "College Verified", "College Verified", "College Verified");

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
