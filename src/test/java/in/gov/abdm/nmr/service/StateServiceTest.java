package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.service.impl.StateServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StateServiceTest {

    @Mock
    IStateRepository stateRepository;

    @InjectMocks
    StateServiceImpl stateService;

    @Test
    void testGetStateDataShouldReturnValidResponse(){
        when(stateRepository.getState(any(BigInteger.class))).thenReturn(List.of(getState()));
        List<StateTO> states = stateService.getStateData(ID);
        assertEquals(1, states.size());
        assertEquals(STATE_ID, states.get(0).getId());
        assertEquals(STATE_NAME, states.get(0).getName());
        assertEquals(ISO_CODE, states.get(0).getIsoCode());
    }

}
