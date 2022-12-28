package in.gov.abdm.nmr.db.sql.domain.nmc_profile;

import java.math.BigInteger;

public interface INmcDaoService {

    NmcProfile findByUserDetail(BigInteger userDetailId);

}
