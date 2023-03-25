package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

/**
 * Enums for all possible actions in NMR
 */
public enum Action {

    SUBMIT(BigInteger.valueOf(1), "Submitted"),
    FORWARD(BigInteger.valueOf(2), "Forwarded"),
    QUERY_RAISE(BigInteger.valueOf(3), "Raise a Query"),
    APPROVED(BigInteger.valueOf(4), "Approved"),
    REJECT(BigInteger.valueOf(5), "Rejected"),
    TEMPORARY_SUSPEND(BigInteger.valueOf(6), "Temporary Suspend"),
    PERMANENT_SUSPEND(BigInteger.valueOf(7), "Permanent Suspend");


    private final BigInteger id;
    private final String description;

    Action(BigInteger id, String description) {
        this.id = id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
