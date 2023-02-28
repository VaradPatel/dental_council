package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.entity.RegistrationDetailsMaster;
import in.gov.abdm.nmr.repository.RegistrationDetailMasterRepository;
import in.gov.abdm.nmr.service.IRegistrationDetailMasterDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Service
@Transactional
public class RegistrationDetailMasterDaoServiceImpl implements IRegistrationDetailMasterDaoService {

    @Autowired
    private RegistrationDetailMasterRepository registrationDetailMasterRepository;

    @Override
    public RegistrationDetailsMaster findByHpProfileId(BigInteger hpProfileId) {
        return registrationDetailMasterRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
    }

}
