package in.gov.abdm.nmr.db.sql.domain.state_medical_council;

import java.util.List;

import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.db.sql.domain.state_medical_council.to.StateMedicalCouncilTO;

@Service
public class StateMedicalCouncilService implements IStateMedicalCouncilService {

    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    private IStateMedicalCouncilMapper stateMedicalCouncilMapper;

    public StateMedicalCouncilService(IStateMedicalCouncilRepository stateMedicalCouncilRepository, IStateMedicalCouncilMapper stateMedicalCouncilMapper) {
        this.stateMedicalCouncilRepository = stateMedicalCouncilRepository;
        this.stateMedicalCouncilMapper = stateMedicalCouncilMapper;
    }

    @Override
    public List<StateMedicalCouncilTO> smcs() {
        return stateMedicalCouncilMapper.stateMedicalCouncilsToDtos(stateMedicalCouncilRepository.findAll());
    }
}
