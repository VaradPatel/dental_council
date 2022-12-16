package in.gov.abdm.nmr.db.sql.domain.state;

import java.math.BigInteger;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class StateService implements IStateService {

    public StateRepository stateRepository;

    private StateDtoMapper stateDtoMapper;

    public StateService(StateRepository stateRepository, StateDtoMapper stateDtoMapper) {
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
