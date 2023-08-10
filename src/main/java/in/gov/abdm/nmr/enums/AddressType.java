package in.gov.abdm.nmr.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * Enums defining different address types used in nmr.
 */
public enum AddressType {

    BUSINESS(1, "Business"),
    CURRENT(2, "Current"),
    PERMANENT(3, "Permanent"),
    COMMUNICATION(4, "Communication"),
    KYC(5, "KYC");

    private final Integer id;
    private final String description;

    AddressType(Integer id, String description) {
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public static AddressType getAddressType(String s) {
        Optional<AddressType> addressType = Arrays.stream(
                AddressType.values()).filter(
                action -> action.getDescription().equalsIgnoreCase(s)).findFirst();
        return addressType.orElse(null);
    }
}
