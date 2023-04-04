package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.entity.NmcProfile;
import in.gov.abdm.nmr.jpa.repository.INmcProfileRepository;
import in.gov.abdm.nmr.service.INmcDaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@Transactional
public class NmcDaoServiceImpl implements INmcDaoService {
    
    private INmcProfileRepository nmcProfileRepository;
    
    public NmcDaoServiceImpl(INmcProfileRepository nmcProfileRepository) {
        this.nmcProfileRepository = nmcProfileRepository;
    }

    @Override
    public NmcProfile findByUserId(BigInteger userId) {
        return nmcProfileRepository.findByUserId(userId);
    }
}
