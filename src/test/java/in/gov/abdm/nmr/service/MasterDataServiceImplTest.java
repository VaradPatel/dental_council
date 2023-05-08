package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.masterdata.MasterDataTO;
import in.gov.abdm.nmr.service.impl.MasterDataServiceImpl;
import in.gov.abdm.nmr.service.impl.StateMedicalCouncilDaoServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.ID;
import static in.gov.abdm.nmr.util.CommonTestData.STATE_MEDICAL_COUNCIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MasterDataServiceImplTest {

    @InjectMocks
    private MasterDataServiceImpl masterDataService;
    @Mock
    StateMedicalCouncilDaoServiceImpl stateMedicalCouncilService;

    @Test
    void testSmcs() {
        when(stateMedicalCouncilService.getAllStateMedicalCouncil())
                .thenReturn(CommonTestData.getStateMedicalCouncilTo());
        List<MasterDataTO> result = masterDataService.smcs();
        assertEquals(ID.longValue(), result.get(0).getId());
        assertNotNull(STATE_MEDICAL_COUNCIL, result.get(0).getName());
    }

}