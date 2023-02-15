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
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class CollegeControllerTest {

    @InjectMocks
    CollegeController collegeController;

    @Mock
    private ICollegeService collegeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        MockMvc mockMvc = standaloneSetup(collegeController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testRegisterCollege() throws Exception {
        CollegeRegistrationRequestTo collegeRegistrationRequestTo = new CollegeRegistrationRequestTo();
        CollegeProfileTo expected = new CollegeProfileTo();
        when(collegeService.registerCollege(BigInteger.ONE, collegeRegistrationRequestTo, false)).thenReturn(expected);
        CollegeProfileTo result = collegeController.registerCollege(collegeRegistrationRequestTo);
        assertEquals(expected, result);
    }


    @Test
    public void testRegisterDean() throws NmrException {
        BigInteger collegeId = new BigInteger("1");
        CollegeDeanCreationRequestTo collegeDeanCreationRequestTo = new CollegeDeanCreationRequestTo();
        CollegeDeanProfileTo expectedResponse = new CollegeDeanProfileTo();
        when(collegeService.registerDean(collegeId, collegeDeanCreationRequestTo)).thenReturn(expectedResponse);
        CollegeDeanProfileTo response = collegeController.registerDean(collegeId, collegeDeanCreationRequestTo);
        assertEquals(expectedResponse, response);
    }

}