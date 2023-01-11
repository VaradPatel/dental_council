package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

/**
 * Enums for all possible actions in NMR
 */
public enum WorkflowStatus {
    PENDING(BigInteger.valueOf(1)),
    APPROVED(BigInteger.valueOf(2)),
    QUERY_RAISED(BigInteger.valueOf(3)),
    REJECTED(BigInteger.valueOf(4)),
    SUSPENDED(BigInteger.valueOf(5)),
    BLACKLISTED(BigInteger.valueOf(6));

    private BigInteger id;
    WorkflowStatus(BigInteger id){
        this.id = id;
    }


    public BigInteger getId() {
        return id;
    }
}