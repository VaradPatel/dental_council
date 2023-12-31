package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Enum for different workflow in NMR.
 */
public enum ApplicationType {
    HP_REGISTRATION(BigInteger.valueOf(1), "HP Registration","registration"),
    HP_MODIFICATION(BigInteger.valueOf(2), "HP Modification","modification"),
    HP_TEMPORARY_SUSPENSION(BigInteger.valueOf(3), "Temporary Suspension","temporary suspension"),
    HP_PERMANENT_SUSPENSION(BigInteger.valueOf(4), "Permanent Suspension", "permanent suspension"),
    HP_ACTIVATE_LICENSE(BigInteger.valueOf(5), "Activate License", "re-activation of licence"),
    COLLEGE_REGISTRATION(BigInteger.valueOf(6), "College Registration","registration"),
    FOREIGN_HP_REGISTRATION(BigInteger.valueOf(7), "Foreign HP Registration", "registration"),
    ADDITIONAL_QUALIFICATION(BigInteger.valueOf(8), "Additional Qualification","additional qualification");

    private final BigInteger id;
    private final String description;
    private final String notifyText;

    ApplicationType(BigInteger id, String description, String notifyText) {
        this.id = id;
        this.description = description;
        this.notifyText = notifyText;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getNotifyText() {
        return notifyText;
    }
    
    public static List<BigInteger> getAllHpApplicationTypeIds() {
        return List.of(HP_REGISTRATION.getId(), HP_MODIFICATION.getId(), HP_TEMPORARY_SUSPENSION.getId(), HP_PERMANENT_SUSPENSION.getId(), HP_ACTIVATE_LICENSE.getId(),
                FOREIGN_HP_REGISTRATION.getId(), ADDITIONAL_QUALIFICATION.getId());
    }

    public static List<BigInteger> getAllCollegeApplicationTypeIds() {
        return List.of(COLLEGE_REGISTRATION.getId());
    }

    public static ApplicationType getApplicationType(BigInteger id) {
        Optional<ApplicationType> optionalApplicationType = Arrays.stream(ApplicationType.values()).filter(action -> action.getId().equals(id)).findFirst();
        return optionalApplicationType.isPresent() ? optionalApplicationType.get() : null;
    }
}