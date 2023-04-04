package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.entity.RegistrationDetails;
import in.gov.abdm.nmr.jpa.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.service.IRegistrationDetailDaoService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;

@Service
@Transactional
public class RegistrationDetailDaoServiceImpl implements IRegistrationDetailDaoService {
    
    private IRegistrationDetailRepository registrationDetailRepository;

    public RegistrationDetailDaoServiceImpl(IRegistrationDetailRepository registrationDetailRepository) {
        this.registrationDetailRepository = registrationDetailRepository;
    }

    @Override
    public RegistrationDetails findByHpProfileId(BigInteger hpProfileId) {
        return registrationDetailRepository.getRegistrationDetailsByHpProfileId(hpProfileId);
    }

}
