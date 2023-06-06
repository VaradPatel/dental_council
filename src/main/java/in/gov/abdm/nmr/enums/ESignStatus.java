package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enums for all possible E sign status in NMR
 */
public enum ESignStatus {

    PROFILE_ESIGNED_WITH_SAME_AADHAR(1, "Profile esigned with same Aadhar"),
    PROFILE_ESIGNED_WITH_DIFFERENT_AADHAR(2, "Profile esigned with different aadhar"),
    PROFILE_NOT_ESIGNED(3, "Profile not esigned");

    private final Integer id;
    private final String status;

    ESignStatus(Integer id, String status) {
        this.id = id;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public static ESignStatus getESignStatus(BigInteger id) {
        Optional<ESignStatus> optionalAction = Arrays.stream(ESignStatus.values()).filter(action -> action.getId().equals(id)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }

    public static ESignStatus getESignStatus(String status) {
        Optional<ESignStatus> optionalAction = Arrays.stream(ESignStatus.values()).filter(action -> action.getStatus().equals(status)).findFirst();
        return optionalAction.isPresent() ? optionalAction.get() : null;
    }
}
