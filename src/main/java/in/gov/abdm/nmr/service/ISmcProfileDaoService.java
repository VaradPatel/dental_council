package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.SMCProfile;

import java.math.BigInteger;

public interface ISmcProfileDaoService {

    SMCProfile findByUserId(BigInteger userId);
}
