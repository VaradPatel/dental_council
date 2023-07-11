package in.gov.abdm.nmr.enums;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public enum AttachmentType {
    CERTIFICATE(BigInteger.valueOf(1)),
    DEGREE(BigInteger.valueOf(2)),
    PROOF(BigInteger.valueOf(3)),
    QUERY_RAISED(BigInteger.valueOf(4)),
    REACTIVATION(BigInteger.valueOf(5));
    private final BigInteger id;

    AttachmentType(BigInteger id) {
        this.id = id;
    }
}
