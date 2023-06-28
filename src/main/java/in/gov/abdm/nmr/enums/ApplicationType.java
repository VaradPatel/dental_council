package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Enum for different workflow in NMR.
 */
public enum ApplicationType {
    HP_REGISTRATION(BigInteger.valueOf(1), "HP Registration"),
    HP_MODIFICATION(BigInteger.valueOf(2), "HP Modification"),
    HP_TEMPORARY_SUSPENSION(BigInteger.valueOf(3), "Temporary Suspension"),
    HP_PERMANENT_SUSPENSION(BigInteger.valueOf(4), "Permanent Suspension"),
    HP_ACTIVATE_LICENSE(BigInteger.valueOf(5), "Activate License"),
    COLLEGE_REGISTRATION(BigInteger.valueOf(6), "College Registration"),
    FOREIGN_HP_REGISTRATION(BigInteger.valueOf(7), "Foreign HP Registration"),
    QUALIFICATION_ADDITION(BigInteger.valueOf(8), "Qualification Addition");

    private final BigInteger id;
    private final String description;

    ApplicationType(BigInteger id, String description) {
        this.id = id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
    
    public static List<BigInteger> getAllHpApplicationTypeIds() {
        return List.of(HP_REGISTRATION.getId(), HP_MODIFICATION.getId(), HP_TEMPORARY_SUSPENSION.getId(), HP_PERMANENT_SUSPENSION.getId(), HP_ACTIVATE_LICENSE.getId(), //
                FOREIGN_HP_REGISTRATION.getId(), QUALIFICATION_ADDITION.getId());
    }

    public static List<BigInteger> getAllCollegeApplicationTypeIds() {
        return List.of(COLLEGE_REGISTRATION.getId());
    }

    public static ApplicationType getApplicationType(BigInteger id) {
        Optional<ApplicationType> optionalApplicationType = Arrays.stream(ApplicationType.values()).filter(action -> action.getId().equals(id)).findFirst();
        return optionalApplicationType.isPresent() ? optionalApplicationType.get() : null;
    }
}