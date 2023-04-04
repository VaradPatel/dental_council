package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.RegistrationDetails;

import java.math.BigInteger;

public interface IRegistrationDetailDaoService {

    RegistrationDetails findByHpProfileId(BigInteger hpProfileId);
}
