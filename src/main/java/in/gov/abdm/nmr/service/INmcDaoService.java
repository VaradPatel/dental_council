package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.NmcProfile;

import java.math.BigInteger;

public interface INmcDaoService {

    NmcProfile findByUserId(BigInteger userId);

}
