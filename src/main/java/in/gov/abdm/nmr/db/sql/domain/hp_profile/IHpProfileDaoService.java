package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.math.BigInteger;

public interface IHpProfileDaoService {

    HpProfile findByUserDetail(BigInteger userDetailId);
}
