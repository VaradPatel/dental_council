package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.DistrictTO;
import in.gov.abdm.nmr.repository.DistrictRepository;
import in.gov.abdm.nmr.service.impl.DistrictServiceImpl;
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
class DistrictServiceTest {

    @Mock
    DistrictRepository districtRepository;

    @InjectMocks
    DistrictServiceImpl districtService;

    @Test
    void testGetDistrictDataShouldReturnListOfDistricts(){
        when(districtRepository.getDistrict(any(BigInteger.class))).thenReturn(List.of(getDistrict()));
        List<DistrictTO> districts = districtService.getDistrictData(ID);
        assertTrue(districts.size() == 1);
        DistrictTO districtTO = districts.get(0);
        assertEquals(ID, districtTO.getId());
        assertEquals(DISTRICT_NAME, districtTO.getName());
        assertEquals(ISO_CODE, districtTO.getIsoCode());
    }

}
