package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.HpProfile;

import java.math.BigInteger;

public interface IHpProfileDaoService {

    HpProfile findByUserDetail(BigInteger userDetailId);
}
