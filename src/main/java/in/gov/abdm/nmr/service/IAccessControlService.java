package in.gov.abdm.nmr.service;

import java.math.BigInteger;

public interface IAccessControlService {

    void validateUser(BigInteger userId);
}
