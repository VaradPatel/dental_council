package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import in.gov.abdm.nmr.mapper.StateDtoMapper;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.service.IStateService;
import org.springframework.stereotype.Service;

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
//        List<Tuple> stateList = stateRepository.getState(countryId);
//        List<StateTO> stateTOList = new ArrayList<StateTO>();
//        for (Tuple state : stateList) {
//            StateTO stateTO = new StateTO();
//            stateTO.setId(state.get("id", BigInteger.class));
//            stateTO.setName(state.get("name", String.class));
//            stateTOList.add(stateTO);
//        }
//        return stateTOList;
    	return stateDtoMapper.stateDataToDto(stateRepository.getState(countryId));
    }

}
