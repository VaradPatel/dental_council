package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enums for all possible actions in NMR
 */
public enum DashboardStatus {

    PENDING(BigInteger.valueOf(1), "Pending", "Pending"),
    FORWARD(BigInteger.valueOf(2), "Forwarded", "Forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Query Raised", "Query Raised"),
    APPROVED(BigInteger.valueOf(4), "Approved", "Approved"),
    REJECT(BigInteger.valueOf(5), "Rejected", "Rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Temporary Suspend", "Blacklisted"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Permanent Suspend", "Suspended"),
    COLLEGE_VERIFIED(BigInteger.valueOf(8), "college_verified", "college_verified");

    private final BigInteger id;
    private final String description;
    private final String status;

    DashboardStatus(BigInteger id, String description, String status) {
        this.id = id;
        this.description = description;
        this.status = status;
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

    public static DashboardStatus getDashboardStatus(BigInteger id) {
        Optional<DashboardStatus> optionalAction = Arrays.stream(DashboardStatus.values()).filter(action -> action.getId().equals(id)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }

    public static DashboardStatus getDashboardStatus(String status) {
        Optional<DashboardStatus> optionalAction = Arrays.stream(DashboardStatus.values()).filter(action -> action.getStatus().equals(status)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }
}
