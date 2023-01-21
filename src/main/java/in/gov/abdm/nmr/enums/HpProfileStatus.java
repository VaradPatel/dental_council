package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

public enum HpProfileStatus {
    PENDING(BigInteger.valueOf(1)),
    APPROVED(BigInteger.valueOf(2)),
    QUERY_RAISED(BigInteger.valueOf(3)),
    REJECTED(BigInteger.valueOf(4)),
    SUSPENDED(BigInteger.valueOf(5)),
    BLACKLISTED(BigInteger.valueOf(6)),
    DRAFT(BigInteger.valueOf(7));

    private BigInteger id;
    HpProfileStatus(BigInteger id){
        this.id = id;
    }


    public BigInteger getId() {
        return id;
    }
}
