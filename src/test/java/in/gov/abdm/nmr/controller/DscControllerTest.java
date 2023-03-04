package in.gov.abdm.nmr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.dto.dsc.DscRequestTo;
import in.gov.abdm.nmr.dto.dsc.DscResponseTo;
import in.gov.abdm.nmr.service.IDscService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DscControllerTest {

    @InjectMocks
    DscController dscController;
    @Mock
    private IDscService dscService;
    DscRequestTo dscRequestTo;
    DscResponseTo expectedResponse;

    @BeforeEach
    void setUp() {
        dscRequestTo = new DscRequestTo();
        expectedResponse = new DscResponseTo();
    }

    @AfterEach
    void tearDown() {
        dscRequestTo = null;
        expectedResponse = null;
    }

    @Test
    void testInvokeDSCGenEspRequest() throws JsonProcessingException {
        when(dscService.invokeDSCGenEspRequest(dscRequestTo)).thenReturn(expectedResponse);
        DscResponseTo actualResponse = dscController.invokeDSCGenEspRequest(dscRequestTo);
        assertEquals(expectedResponse, actualResponse);
    }
}