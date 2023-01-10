package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

public enum WorkFlowStatusEnum {

    PENDING(BigInteger.valueOf(1),"Pending"),
    APPROVED(BigInteger.valueOf(2),"Approved"),
    QUERY_RAISED(BigInteger.valueOf(3),"Query Raised"),
    REJECTED(BigInteger.valueOf(4),"Rejected"),
    SUSPENDED(BigInteger.valueOf(5),"Suspended"),
    BLACKLISTED(BigInteger.valueOf(6),"Blacklisted");
    private final BigInteger id;
    private final String description;

    WorkFlowStatusEnum(BigInteger id, String description){
        this.id= id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
