package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.NbeProfile;

import java.math.BigInteger;

public interface INbeDaoService {
    NbeProfile findByUserDetail(BigInteger userDetailId);
}
