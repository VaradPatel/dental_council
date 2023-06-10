package in.gov.abdm.nmr.enums;

import in.gov.abdm.nmr.exception.InvalidRequestException;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Enums for all possible actions in NMR
 */
public enum DashboardStatus {

    PENDING(BigInteger.valueOf(1), "Pending", "Pending"),
    FORWARD(BigInteger.valueOf(2), "Forwarded", "Forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Query Raised", "Query Raised"),
    APPROVED(BigInteger.valueOf(4), "Approved", "Approved"),
    REJECT(BigInteger.valueOf(5), "Rejected", "Rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Blacklisted", "Blacklisted"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Suspended", "Suspended"),
    COLLEGE_VERIFIED(BigInteger.valueOf(8), "College Verified", "College Verified");

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
        return Arrays.stream(DashboardStatus.values()).filter(action -> action.getId().equals(id)).findFirst().orElseThrow();

    }

    public static DashboardStatus getDashboardStatus(String status){
        return Arrays.stream(DashboardStatus.values()).filter(action -> action.getStatus().equals(status)).findFirst().orElseThrow();
    }
}
