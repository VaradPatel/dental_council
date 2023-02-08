package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import java.math.BigInteger;

public interface IRegistrationDetailMasterDaoService {

    RegistrationDetailsMaster findByHpProfileId(BigInteger hpProfileId);
}
