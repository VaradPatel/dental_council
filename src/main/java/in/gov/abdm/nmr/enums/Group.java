package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

/**
 * Enum for Actor Groups
 */
public enum Group {
    HEALTH_PROFESSIONAL(BigInteger.valueOf(1), "Health Professional"),
    SMC(BigInteger.valueOf(2), "State Medical Council"),
    NMC(BigInteger.valueOf(3), "National Medical Council"),
    COLLEGE_DEAN(BigInteger.valueOf(4), "College Dean"),
    COLLEGE_REGISTRAR(BigInteger.valueOf(5), "College Registrar"),
    COLLEGE_ADMIN(BigInteger.valueOf(6), "College Admin"),
    NBE(BigInteger.valueOf(7), "NBE");

    private final BigInteger id;
    private final String description;

    Group(BigInteger id, String description) {
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
