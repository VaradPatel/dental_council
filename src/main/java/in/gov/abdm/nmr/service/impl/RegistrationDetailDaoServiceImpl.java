package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.entity.RegistrationDetails;
import in.gov.abdm.nmr.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.service.IRegistrationDetailDaoService;

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
