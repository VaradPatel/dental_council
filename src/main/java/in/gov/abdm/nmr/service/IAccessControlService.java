package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.User;

import java.math.BigInteger;

public interface IAccessControlService {

    void validateUser(BigInteger userId);

    User getLoggedInUser();
}
