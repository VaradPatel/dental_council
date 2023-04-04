package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.jpa.repository.IQualificationDetailRepository;
import in.gov.abdm.nmr.service.IQualificationDetailDaoService;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class QualificationDetailDaoServiceImpl implements IQualificationDetailDaoService {
    
    private IQualificationDetailRepository qualificationDetailRepository;

    public QualificationDetailDaoServiceImpl(IQualificationDetailRepository qualificationDetailRepository) {
        this.qualificationDetailRepository = qualificationDetailRepository;
    }

    @Override
    public List<Tuple> findSearchQualificationDetailsByHpProfileId(BigInteger hpprofileId) {
        return qualificationDetailRepository.findSearchQualificationDetailsByHpProfileId(hpprofileId);
    }
}
