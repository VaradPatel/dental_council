package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.SubDistrictTO;
import in.gov.abdm.nmr.entity.SubDistrict;
import in.gov.abdm.nmr.repository.SubDistrictRepository;
import in.gov.abdm.nmr.service.impl.SubDistrictServiceImpl;
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
class SubDistrictServiceTest {

    @Mock
    SubDistrictRepository subDistrictRepository;

    @InjectMocks
    SubDistrictServiceImpl subDistrictService;

    @Test
    void testGetStateDataShouldReturnValidResponse(){
        when(subDistrictRepository.getSubDistrict(any(BigInteger.class))).thenReturn(List.of(getSubDistrict()));
        List<SubDistrictTO> subDistricts = subDistrictService.getSubDistrictData(ID);
        assertEquals(1, subDistricts.size());
        assertEquals(ID, subDistricts.get(0).getId());
        assertEquals(SUB_DISTRICT_NAME, subDistricts.get(0).getName());
        assertEquals(ISO_CODE, subDistricts.get(0).getIsoCode());
    }

}
