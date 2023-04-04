package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.HpProfileMaster;

import java.math.BigInteger;

public interface IHpProfileMasterDaoService {

    HpProfileMaster findHpProfileMasterById(BigInteger hpProfileId);

}
