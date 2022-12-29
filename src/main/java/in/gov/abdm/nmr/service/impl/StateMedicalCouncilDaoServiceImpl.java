package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;

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
    
    @Override
    public StateMedicalCouncil findByUserDetail(BigInteger userDetailId) {
        return stateMedicalCouncilRepository.findByUserDetail(userDetailId);
    }
}
