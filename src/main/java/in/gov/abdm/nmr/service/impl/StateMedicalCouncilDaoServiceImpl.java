package in.gov.abdm.nmr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;

@Service
@Transactional
public class StateMedicalCouncilDaoServiceImpl implements IStateMedicalCouncilDaoService {

    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    private IStateMedicalCouncilMapper stateMedicalCouncilMapper;

    public StateMedicalCouncilDaoServiceImpl(IStateMedicalCouncilRepository stateMedicalCouncilRepository, IStateMedicalCouncilMapper stateMedicalCouncilMapper) {
        this.stateMedicalCouncilRepository = stateMedicalCouncilRepository;
        this.stateMedicalCouncilMapper = stateMedicalCouncilMapper;
    }

    @Override
    public List<StateMedicalCouncilTO> smcs() {
        return stateMedicalCouncilMapper.stateMedicalCouncilsToDtos(stateMedicalCouncilRepository.findAll());
    }
}
