package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.CollegeMaster;
import in.gov.abdm.nmr.repository.ICollegeMasterRepository;
import in.gov.abdm.nmr.service.impl.CollegeMasterDaoServiceImpl;
import in.gov.abdm.nmr.util.CommonTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollegeMasterDaoServiceTest {

    @Mock
    ICollegeMasterRepository collegeMasterRepository;

    @InjectMocks
    CollegeMasterDaoServiceImpl collegeMasterDaoService;

    CollegeMaster collegeMaster = null;

    @BeforeEach
    void setup(){
        collegeMaster = CommonTestData.getCollegeMaster();
    }

    @Test
    void testFindByIdShouldReturnCollegeWhenIdExists(){
        when(collegeMasterRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(collegeMaster));
        CollegeMaster collegeMaster = collegeMasterDaoService.findById(ID);
        assertEquals(ID, collegeMaster.getId());
        assertEquals(COLLEGE_CODE, collegeMaster.getCollegeCode());
        assertEquals(COLLEGE_NAME, collegeMaster.getName());
        assertEquals(COLLEGE_VISIBLE_STATUS, collegeMaster.getVisibleStatus());
        assertEquals(SYSTEM_OF_MEDICINE, collegeMaster.getSystemOfMedicineId());
        assertEquals(STATE_ID, collegeMaster.getState().getId());
        assertEquals(STATE_NAME, collegeMaster.getState().getName());
        assertEquals(STATE_MEDICAL_COUNCIL, collegeMaster.getStateMedicalCouncil().getName());
        assertEquals(COURSE_NAME, collegeMaster.getCourse().getCourseName());
        assertEquals(WEBSITE, collegeMaster.getWebsite());
        assertEquals(ADDRESS_LINE_1, collegeMaster.getAddressLine1());
        assertEquals(ADDRESS_LINE_2, collegeMaster.getAddressLine2());
        assertEquals(DISTRICT_NAME, collegeMaster.getDistrict().getName());
        assertEquals(VILLAGE_NAME, collegeMaster.getVillage().getName());
        assertEquals(PIN_CODE, collegeMaster.getPinCode());

    }

    @Test
    void testGetAllCollegesShouldReturnListOfColleges(){
        when(collegeMasterRepository.getColleges()).thenReturn(List.of(collegeMaster));
        List<CollegeMaster> colleges = collegeMasterDaoService.getAllColleges();
        assertEquals(1, colleges.size());
    }

    @Test
    void testSaveCollegeShouldSaveCollege(){
        when(collegeMasterRepository.save(any(CollegeMaster.class))).thenReturn(collegeMaster);
        CollegeMaster result = collegeMasterDaoService.save(collegeMaster);
        assertNotNull(result);
    }


}
