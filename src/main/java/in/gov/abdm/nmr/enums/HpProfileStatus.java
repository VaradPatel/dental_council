package in.gov.abdm.nmr.enums;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

public enum HpProfileStatus {
    PENDING(BigInteger.valueOf(1), "Pending"),
    APPROVED(BigInteger.valueOf(2), "Approved"),
    QUERY_RAISED(BigInteger.valueOf(3), "Query Raised"),
    REJECTED(BigInteger.valueOf(4), "Rejected"),
    SUSPENDED(BigInteger.valueOf(5), "Suspended"),
    BLACKLISTED(BigInteger.valueOf(6), "Blacklisted"),
    DRAFT(BigInteger.valueOf(7), "draft");

    private BigInteger id;
    private final String description;


    HpProfileStatus(BigInteger id, String description) {
        this.id = id;
        this.description = description;
    }

    public BigInteger getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static HpProfileStatus getHpProfileStatus(BigInteger id) {
        Optional<HpProfileStatus> optionalHpProfileStatus = Arrays.stream(HpProfileStatus.values()).filter(profileStatus -> profileStatus.getId().equals(id)).findFirst();
        return optionalHpProfileStatus.isPresent() ? optionalHpProfileStatus.get() : null;
    }
}
