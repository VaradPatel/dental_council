package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Enum for Actor Groups
 */
public enum Group {
    HEALTH_PROFESSIONAL(BigInteger.valueOf(1), "Health Professional"),
    SMC(BigInteger.valueOf(2), "State Medical Council"),
    NMC(BigInteger.valueOf(3), "National Medical Council"),
    COLLEGE(BigInteger.valueOf(4), "College"),
//    COLLEGE_REGISTRAR(BigInteger.valueOf(5), "College Registrar"),
//    COLLEGE_ADMIN(BigInteger.valueOf(6), "College Admin"),
    NBE(BigInteger.valueOf(7), "NBE"),
    SYSTEM(BigInteger.valueOf(8), "SYSTEM");

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

    public static Group getGroup(BigInteger id) {
        return Arrays.stream(Group.values()).filter(group -> group.getId().equals(id)).findFirst().get();
    }

}
