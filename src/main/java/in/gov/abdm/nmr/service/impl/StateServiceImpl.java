package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.mapper.StateDtoMapper;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.service.IStateService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;

@Service
@Transactional
public class StateServiceImpl implements IStateService {

    public IStateRepository stateRepository;

    private StateDtoMapper stateDtoMapper;

    public StateServiceImpl(IStateRepository stateRepository, StateDtoMapper stateDtoMapper) {
        this.stateRepository = stateRepository;
        this.stateDtoMapper = stateDtoMapper;
    }

    @Override
    public List<StateTO> getStateData(BigInteger countryId) {
    	return stateDtoMapper.stateDataToDto(stateRepository.getState(countryId));
    }

}
