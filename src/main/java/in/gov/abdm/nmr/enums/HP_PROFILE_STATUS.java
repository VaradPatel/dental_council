package in.gov.abdm.nmr.enums;

import java.math.BigInteger;

public enum HP_PROFILE_STATUS {
    REJECTED(BigInteger.valueOf(4)), APPROVED(BigInteger.valueOf(2));

    private BigInteger id;
    HP_PROFILE_STATUS(BigInteger id){
        this.id = id;
    }


    public BigInteger getId() {
        return id;
    }
}
