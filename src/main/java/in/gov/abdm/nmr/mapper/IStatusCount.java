package in.gov.abdm.nmr.mapper;

import java.math.BigInteger;

public interface IStatusCount {
    BigInteger getApplicationTypeId();

    BigInteger getProfileStatus();

    BigInteger getCount();
}
