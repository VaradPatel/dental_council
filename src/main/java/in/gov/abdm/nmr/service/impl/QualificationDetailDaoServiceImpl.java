package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.repository.IQualificationDetailRepository;
import in.gov.abdm.nmr.service.IQualificationDetailDaoService;

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
