package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.RegistrationRenewationTypeTO;
import in.gov.abdm.nmr.dto.StateTO;
import in.gov.abdm.nmr.repository.IStateRepository;
import in.gov.abdm.nmr.repository.RegistrationRenewationTypeRepository;
import in.gov.abdm.nmr.service.impl.RegistrationRenewationTypeServiceImpl;
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
class RegistrationRenewationTypeServiceTest {

    @Mock
    RegistrationRenewationTypeRepository registrationRenewationTypeRepository;

    @InjectMocks
    RegistrationRenewationTypeServiceImpl registrationRenewationTypeService;

    @Test
    void testGetStateDataShouldReturnValidResponse(){
        when(registrationRenewationTypeRepository.getRegistrationRenewationType()).thenReturn(List.of(getRegistrationRenewationType()));
        List<RegistrationRenewationTypeTO> registrationRenewationType = registrationRenewationTypeService.getRegistrationRenewationType();

        assertEquals(1, registrationRenewationType.size());
        assertEquals(ID, registrationRenewationType.get(0).getId());
        assertEquals(PERMANENT_RENEWATION, registrationRenewationType.get(0).getName());

    }

}
