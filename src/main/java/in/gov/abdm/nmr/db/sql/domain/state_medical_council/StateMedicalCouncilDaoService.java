package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

@Service
@Transactional
public class StateMedicalCouncilDaoService implements IStateMedicalCouncilDaoService {

    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    private IStateMedicalCouncilMapper stateMedicalCouncilMapper;

    public StateMedicalCouncilDaoService(IStateMedicalCouncilRepository stateMedicalCouncilRepository, IStateMedicalCouncilMapper stateMedicalCouncilMapper) {
        this.stateMedicalCouncilRepository = stateMedicalCouncilRepository;
        this.stateMedicalCouncilMapper = stateMedicalCouncilMapper;
    }

    @Override
    public List<StateMedicalCouncilTO> smcs() {
        return stateMedicalCouncilMapper.stateMedicalCouncilsToDtos(stateMedicalCouncilRepository.findAll());
    }
    
    @Override
    public StateMedicalCouncil findByUserDetail(BigInteger userDetailId) {
        return stateMedicalCouncilRepository.findByUserDetail(userDetailId);
    }
}
