package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.service.ISmcProfileDaoService;

@Service
@Transactional
public class SmcProfileDaoServiceImpl implements ISmcProfileDaoService {

    private ISmcProfileRepository smcProfileRepository;

    public SmcProfileDaoServiceImpl(ISmcProfileRepository smcProfileRepository) {
        this.smcProfileRepository = smcProfileRepository;
    }

    @Override
    public SMCProfile findByUserId(BigInteger userId) {
        return smcProfileRepository.findByUserId(userId);
    }

}
