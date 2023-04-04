package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.jpa.entity.ApplicationType;
import in.gov.abdm.nmr.jpa.entity.RequestCounter;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.jpa.repository.IRequestCounterRepository;
import in.gov.abdm.nmr.service.impl.RequestCounterServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestCounterServiceTest {

    private static final BigInteger INVALID_APPLICATION_TYPE_ID = BigInteger.valueOf(999999999);

    @InjectMocks
    RequestCounterServiceImpl requestCounterService;

    @Mock
    IRequestCounterRepository requestCounterRepositoryMock;

    @Test
    void testIncrementAndRetrieveCountShouldReturnIncrementedCount() throws WorkFlowException {
        RequestCounter requestCounter = RequestCounter.builder().counter(BigInteger.valueOf(1)).applicationType(ApplicationType.builder().build()).build();
        when(requestCounterRepositoryMock.findById(any(BigInteger.class))).thenReturn(Optional.of(requestCounter));
        RequestCounter requestCounter1 = requestCounterService.incrementAndRetrieveCount(in.gov.abdm.nmr.enums.ApplicationType.HP_REGISTRATION.getId());
        assertEquals(BigInteger.valueOf(2), requestCounter1.getCounter());
    }

    @Test
    void testIncrementAndRetrieveCountShouldThrowExceptinoForInvalidApplicationTypeId() throws Exception{
        when(requestCounterRepositoryMock.findById(any(BigInteger.class))).thenReturn(Optional.empty());
        assertThrows(WorkFlowException.class, () -> requestCounterService.incrementAndRetrieveCount(INVALID_APPLICATION_TYPE_ID));
    }
}
