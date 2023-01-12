package in.gov.abdm.nmr.service;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.RegistrationDetails;

public interface IRegistrationDetailDaoService {

    RegistrationDetails findByHpProfileId(BigInteger hpProfileId);
}
