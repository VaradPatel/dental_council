package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.CollegeDeanCreationRequestTo;
import in.gov.abdm.nmr.dto.CollegeDeanProfileTo;
import in.gov.abdm.nmr.dto.CollegeProfileTo;
import in.gov.abdm.nmr.dto.CollegeRegistrationRequestTo;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.service.ICollegeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CollegeControllerTest {

    @InjectMocks
    CollegeController collegeController;
    @Mock
    private ICollegeService collegeService;
    CollegeRegistrationRequestTo collegeRegistrationRequestTo;
    CollegeDeanCreationRequestTo collegeDeanCreationRequestTo;
    BigInteger collegeId;
    CollegeDeanProfileTo expectedResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        collegeRegistrationRequestTo = new CollegeRegistrationRequestTo();
        collegeDeanCreationRequestTo = new CollegeDeanCreationRequestTo();
        collegeId = BigInteger.valueOf(1);
        expectedResponse = new CollegeDeanProfileTo();
    }

    @AfterEach
    void tearDown() {
        collegeRegistrationRequestTo = null;
        collegeDeanCreationRequestTo = null;
        expectedResponse = null;
    }

    @Test
    void testRegisterCollege() throws Exception {
        CollegeProfileTo expected = new CollegeProfileTo();
        when(collegeService.registerCollege(null, collegeRegistrationRequestTo, false)).thenReturn(expected);
        CollegeProfileTo result = collegeController.registerCollege(collegeRegistrationRequestTo);
        assertEquals(expected, result);
    }


    @Test
    void testRegisterDean() throws NmrException {
        when(collegeService.registerDean(collegeId, collegeDeanCreationRequestTo)).thenReturn(expectedResponse);
        CollegeDeanProfileTo response = collegeController.registerDean(collegeId, collegeDeanCreationRequestTo);
        assertEquals(expectedResponse, response);
    }

}