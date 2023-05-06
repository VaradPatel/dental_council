package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.UniversityMaster;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.mapper.IUniversityMasterToMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.UniversityServiceImpl;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniversityServiceTest {

    @InjectMocks
    UniversityServiceImpl universityService;
    @Mock
    private IUniversityMasterToMapper universititesToMapper;
    @Mock
    private UniversityMasterRepository universitiesRepository;

    @Test
    void testFindById() {
        when(universitiesRepository.findById(ID)).thenReturn(Optional.of(getUniversityMaster()));
        UniversityMaster universityMaster = universityService.findById(ID);
        assertEquals(ID, universityMaster.getId());

    }




}