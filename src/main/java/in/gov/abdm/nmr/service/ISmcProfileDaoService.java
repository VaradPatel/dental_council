package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.SMCProfile;

public interface ISmcProfileDaoService {

    SMCProfile findByUserDetail(BigInteger userDetailId);
}
