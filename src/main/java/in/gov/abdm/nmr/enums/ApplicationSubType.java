package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

public enum ApplicationSubType {
    SELF_REACTIVATION(BigInteger.valueOf(1), "Self Reactivation"),
    REACTIVATION_THROUGH_SMC(BigInteger.valueOf(2), "Reactivation Through SMC");

    private final BigInteger id;
    private final String description;

    ApplicationSubType(BigInteger id, String description) {
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
