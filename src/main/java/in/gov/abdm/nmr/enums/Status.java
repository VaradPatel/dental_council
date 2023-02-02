package in.gov.abdm.nmr.enums;

import lombok.Getter;

@Getter
public enum Status {
    APPROVED("Approved"),
    VERIFIED("Verified"),
    PENDING("Pending"),
    SUMBMITTED("Submitted");
    private final String name;

    private Status(String name) {
        this.name = name;
    }
}
