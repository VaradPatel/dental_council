package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.entity.NbeProfile;
import in.gov.abdm.nmr.jpa.repository.INbeProfileRepository;
import in.gov.abdm.nmr.service.INbeDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@Transactional
public class NbeDaoServiceImpl implements INbeDaoService {
    @Autowired
    private INbeProfileRepository nbeProfileRepository;

    @Override
    public NbeProfile findByUserId(BigInteger userId) {
        return nbeProfileRepository.findByUserId(userId);
    }
}
