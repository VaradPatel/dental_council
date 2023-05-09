package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.NmcProfile;

import java.math.BigInteger;

public interface INmcDaoService {

    NmcProfile findByUserId(BigInteger userId);

    NmcProfile save(NmcProfile nmcProfile);

}
