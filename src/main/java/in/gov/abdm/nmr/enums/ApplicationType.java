package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

/**
 * Enum for different workflow in NMR.
 */
public enum ApplicationType {
    HP_REGISTRATION(BigInteger.valueOf(1), "HP Registration"),
    HP_MODIFICATION(BigInteger.valueOf(2), "HP Modification"),
    HP_TEMPORARY_SUSPENSION(BigInteger.valueOf(3), "Temporary Suspension"),
    HP_PERMANENT_SUSPENSION(BigInteger.valueOf(4), "Permanent Suspension"),
    HP_ACTIVATE_LICENSE(BigInteger.valueOf(5), "Activate License"),
    COLLEGE_REGISTRATION(BigInteger.valueOf(6), "College Registration");


    private final BigInteger id;
    private final String description;

    ApplicationType(BigInteger id, String description){
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