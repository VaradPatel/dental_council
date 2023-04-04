package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.entity.HpProfileMaster;
import in.gov.abdm.nmr.jpa.repository.IHpProfileMasterRepository;
import in.gov.abdm.nmr.service.IHpProfileMasterDaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class HpProfileMasterDaoServiceImpl implements IHpProfileMasterDaoService {

    @Autowired
    private  IHpProfileMasterRepository iHpProfileMasterRepository;


    @Override
    public HpProfileMaster findHpProfileMasterById (BigInteger hpProfileId) {
        return iHpProfileMasterRepository.findHpProfileMasterById(hpProfileId);
    }
}
