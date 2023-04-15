package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.VillagesTO;
import in.gov.abdm.nmr.repository.VillagesRepository;
import in.gov.abdm.nmr.service.impl.VillagesServiceImpl;
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
class VillagesServiceTest {

    @Mock
    VillagesRepository villagesRepository;

    @InjectMocks
    VillagesServiceImpl villagesService;

    @Test
    void testGetStateDataShouldReturnValidResponse(){
        when(villagesRepository.getVillage(any(BigInteger.class))).thenReturn(List.of(getVillage()));
        List<VillagesTO> villages = villagesService.getCityData(ID);
        assertTrue(villages.size() == 1);
        assertEquals(ID, villages.get(0).getId());
        assertEquals(VILLAGE_NAME, villages.get(0).getName());
    }

}
