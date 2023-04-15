package in.gov.abdm.nmr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;

@Service
@Transactional
public class StateMedicalCouncilDaoServiceImpl implements IStateMedicalCouncilDaoService {

    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    public StateMedicalCouncilDaoServiceImpl(IStateMedicalCouncilRepository stateMedicalCouncilRepository) {
        this.stateMedicalCouncilRepository = stateMedicalCouncilRepository;
    }

    @Override
    public List<StateMedicalCouncilTO> getAllStateMedicalCouncil() {
        return IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(stateMedicalCouncilRepository.findAll());
    }

    @Override
    public StateMedicalCouncil findByState(String stateId) {
        return stateMedicalCouncilRepository.findByState(stateId);
    }
}
