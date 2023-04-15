package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.BroadSpecialityTO;
import in.gov.abdm.nmr.repository.BroadSpecialityRepository;
import in.gov.abdm.nmr.service.impl.BroadSpecialityServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BroadSpecialityServiceTest {

    @Mock
    BroadSpecialityRepository broadSpecialityRepository;

    @InjectMocks
    BroadSpecialityServiceImpl broadSpecialityService;

    @Test
    void testGetSpecialityDataShouldReturnValidResponse(){
        when(broadSpecialityRepository.getSpeciality()).thenReturn(List.of(getBroadSpeciality()));
        List<BroadSpecialityTO> broadSpecialities = broadSpecialityService.getSpecialityData();
        assertTrue(broadSpecialities.size() == 1);
        assertEquals(ID, broadSpecialities.get(0).getId());
        assertEquals(BROAD_SPECIALITY, broadSpecialities.get(0).getName());
    }

}
