package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enums for all possible actions in NMR
 */
public enum Action {

    SUBMIT(BigInteger.valueOf(1), "Submitted", "Pending", "submitted"),
    FORWARD(BigInteger.valueOf(2), "Forwarded", "Forwarded","forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Raise a Query", "Query Raised","returned with query"),
    APPROVED(BigInteger.valueOf(4), "Approved", "Approved","approved"),
    REJECT(BigInteger.valueOf(5), "Rejected", "Rejected","rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Temporary Suspend", "Blacklisted","temporarily suspended"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Permanent Suspend", "Suspended","permanently suspended");

    private final BigInteger id;
    private final String description;
    private final String status;
    private final String notifyText;

    Action(BigInteger id, String description, String status, String notifyText) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.notifyText = notifyText;
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

    public String getNotifyText() {
        return notifyText;
    }

    public static Action getAction(BigInteger id) {
        Optional<Action> optionalAction = Arrays.stream(Action.values()).filter(action -> action.getId().equals(id)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }

    public static Action getAction(String status) {
        Optional<Action> optionalAction = Arrays.stream(Action.values()).filter(action -> action.getStatus().equals(status)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }
}
