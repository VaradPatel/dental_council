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

    private IStateRepository stateRepository;

    public StateServiceImpl(IStateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<StateTO> getStateData(BigInteger countryId) {
    	return StateDtoMapper.STATE_DTO_MAPPER.stateDataToDto(stateRepository.getState(countryId));
    }

}
