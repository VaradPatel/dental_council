package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Enums for all possible actions in NMR
 */
public enum Action {

    SUBMIT(BigInteger.valueOf(1), "Submitted", "Pending"),
    FORWARD(BigInteger.valueOf(2), "Forwarded", "Forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Raise a Query", "Query Raised"),
    APPROVED(BigInteger.valueOf(4), "Approved", "Approved"),
    REJECT(BigInteger.valueOf(5), "Rejected", "Rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Temporary Suspend", "Blacklisted"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Permanent Suspend", "Suspended");


    private final BigInteger id;
    private final String description;
    private final String status;

    Action(BigInteger id, String description, String status) {
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

    public static Action getAction(BigInteger id) {
        return Arrays.stream(Action.values()).filter(action -> action.getId().equals(id)).findFirst().get();
    }

    public static Action getAction(String status) {
        return Arrays.stream(Action.values()).filter(action -> action.getStatus().equals(status)).findFirst().get();
    }


}
