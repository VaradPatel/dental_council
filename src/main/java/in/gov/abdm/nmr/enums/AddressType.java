package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

/**
 * Enums defining different address types used in nmr.
 */
public enum AddressType {

    BUSINESS(1, "Business"),
    CURRENT(2, "Current"),
    PERMANENT(3, "Permanent"),
    COMMUNICATION(4, "Communication");

    private final Integer id;
    private final String description;

    AddressType(Integer id, String description){
        this.id = id;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
