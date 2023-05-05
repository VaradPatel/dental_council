package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.repository.IHpProfileMasterRepository;
import in.gov.abdm.nmr.service.impl.HpProfileMasterDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HpProfileMasterDaoServiceTest {
    @InjectMocks
    HpProfileMasterDaoServiceImpl hpProfileMasterDaoService;
    @Mock
    IHpProfileMasterRepository iHpProfileMasterRepository;

    @Test
    void testFindHpProfileMasterByIdShouldReturnMasterHpProfile(){
        when(iHpProfileMasterRepository.findHpProfileMasterById(any(BigInteger.class))).thenReturn(getMasterHpProfile());
        HpProfileMaster hpProfileMaster = hpProfileMasterDaoService.findHpProfileMasterById(ID);
        assertEquals(ID, hpProfileMaster.getId());
        assertEquals(PROFILE_DISPLAY_NAME, hpProfileMaster.getFullName());


    }
}
