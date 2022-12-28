package in.gov.abdm.nmr.db.sql.domain.nmc_profile;

import java.math.BigInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NmcDaoService implements INmcDaoService {
    
    private INmcProfileRepository nmcProfileRepository;
    
    public NmcDaoService(INmcProfileRepository nmcProfileRepository) {
        this.nmcProfileRepository = nmcProfileRepository;
    }

    @Override
    public NmcProfile findByUserDetail(BigInteger userDetailId) {
        return nmcProfileRepository.findByUserDetail(userDetailId);
    }
}
