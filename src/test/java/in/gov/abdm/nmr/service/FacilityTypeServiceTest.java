package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.FacilityTypeTO;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.entity.FacilityType;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.HpProfileMaster;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.mapper.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.service.impl.FacilityTypeServiceImpl;
import in.gov.abdm.nmr.service.impl.WorkflowPostProcessorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FacilityTypeServiceTest {

    @InjectMocks
    FacilityTypeServiceImpl facilityTypeService;
    @Mock
    private FacilityTypeRepository facilityTypeRepository;
    @Mock
    private FacilityTypeDtoMapper facilityTypeDtoMapper;

    List<FacilityType> facilityTypes;


    @BeforeEach
    void setup() {
        facilityTypes = new ArrayList<>();
        FacilityType facilityType = new FacilityType();
        facilityType.setId(ID);
        facilityType.setName(FACILITY_NAME);
        facilityTypes.add(facilityType);
    }

    @Test
    void testGetFacilityType() {
        when(facilityTypeRepository.getFacilityType()).thenReturn(facilityTypes);
        List<FacilityTypeTO> facilityType = facilityTypeService.getFacilityType();
        assertEquals(1, facilityType.size());
        assertEquals(ID, facilityType.get(0).getId());
        assertEquals(FACILITY_NAME, facilityType.get(0).getName());
    }
}