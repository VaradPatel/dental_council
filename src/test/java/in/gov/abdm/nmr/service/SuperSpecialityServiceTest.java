package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.SuperSpecialityTO;
import in.gov.abdm.nmr.repository.SuperSpecialityRepository;
import in.gov.abdm.nmr.service.impl.SuperSpecialityServiceImpl;
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
class SuperSpecialityServiceTest {

    @Mock
    SuperSpecialityRepository superSpecialityRepository;

    @InjectMocks
    SuperSpecialityServiceImpl superSpecialityService;

    @Test
    void testGetSpecialityDataShouldReturnValidResponse(){
        when(superSpecialityRepository.getSpeciality()).thenReturn(getSuperSpeciality());
        List<SuperSpecialityTO> superSpecialities = superSpecialityService.getSpecialityData();
        assertEquals(1, superSpecialities.size());
        assertEquals(ID, superSpecialities.get(0).getId());
        assertEquals(SUPER_SPECIALITY, superSpecialities.get(0).getName());
    }

}
