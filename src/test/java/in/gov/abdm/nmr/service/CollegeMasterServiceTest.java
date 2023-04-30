package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.CollegeMasterTo;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.impl.CollegeMasterServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollegeMasterServiceTest {

    @InjectMocks
    CollegeMasterServiceImpl collegeMasterService;

    @Mock
    ICollegeMasterRepository collegeMaster;


    @Test
    void testGetCollegesByStateIdReturnsListOfCollegesForGivenStateId(){
        when(collegeMaster.getCollegesByStateId(any(BigInteger.class))).thenReturn(List.of(CommonTestData.getCollegeMaster()));
        List<CollegeMasterTo> colleges = collegeMasterService.getCollegesByStateId(ID);
        assertEquals(1, colleges.size());
        CollegeMasterTo collegeMasterTo = colleges.get(0);
        assertEquals(ID, collegeMasterTo.getId());
        assertEquals(COLLEGE_NAME,collegeMasterTo.getName());

    }
}
