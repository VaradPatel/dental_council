package in.gov.abdm.nmr.service.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.StateMedicalCouncilTO;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.mapper.IStateMedicalCouncilMapper;
import in.gov.abdm.nmr.repository.IStateMedicalCouncilRepository;
import in.gov.abdm.nmr.service.IStateMedicalCouncilDaoService;

@Service
@Transactional
@Slf4j
public class StateMedicalCouncilDaoServiceImpl implements IStateMedicalCouncilDaoService {

    private IStateMedicalCouncilRepository stateMedicalCouncilRepository;

    public StateMedicalCouncilDaoServiceImpl(IStateMedicalCouncilRepository stateMedicalCouncilRepository) {
        this.stateMedicalCouncilRepository = stateMedicalCouncilRepository;
    }

    @Override
    public List<StateMedicalCouncilTO> getAllStateMedicalCouncil() {
        log.info("Fetching state-medical-council details.");
        return IStateMedicalCouncilMapper.STATE_MEDICAL_COUNCIL_MAPPER.stateMedicalCouncilsToDtos(stateMedicalCouncilRepository.findAll(Sort.by("name").ascending()));
    }

    @Override
    public StateMedicalCouncil findByState(String stateId) {
        return stateMedicalCouncilRepository.findByState(stateId);
    }
}
