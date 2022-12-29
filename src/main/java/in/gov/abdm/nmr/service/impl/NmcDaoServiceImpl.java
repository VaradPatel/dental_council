package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.service.INmcDaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NmcDaoServiceImpl implements INmcDaoService {
    
    private INmcProfileRepository nmcProfileRepository;
    
    public NmcDaoServiceImpl(INmcProfileRepository nmcProfileRepository) {
        this.nmcProfileRepository = nmcProfileRepository;
    }

    @Override
    public NmcProfile findByUserDetail(BigInteger userDetailId) {
        return nmcProfileRepository.findByUserDetail(userDetailId);
    }
}
