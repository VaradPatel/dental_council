package in.gov.abdm.nmr.controller;/*
package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ActionRequestTo;
import in.gov.abdm.nmr.dto.ReactivateHealthProfessionalResponseTO;
import in.gov.abdm.nmr.exception.WorkFlowException;
import in.gov.abdm.nmr.service.IActionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ActionControllerTest {
    @InjectMocks
    ActionController actionController;
    @Mock
    private IActionService iActionService;
    ReactivateHealthProfessionalResponseTO reactivateHealthProfessionalResponseTO;
    ActionRequestTo actionRequestTo;

    @BeforeEach
    public void setUp() {
        iActionService = mock(IActionService.class);
        actionController = new ActionController(iActionService);
        actionRequestTo = new ActionRequestTo();
        reactivateHealthProfessionalResponseTO = new ReactivateHealthProfessionalResponseTO();
    }

    @AfterEach
    void tearDown() {
        actionRequestTo = null;
        reactivateHealthProfessionalResponseTO = null;
    }


    @Test
    public void testSuspensionHealthProfessional() throws WorkFlowException {
        when(iActionService.suspendRequest(actionRequestTo)).thenReturn("Suspended");
        String result = actionController.suspensionHealthProfessional(actionRequestTo);
        assertEquals("Suspended", result);
    }

    @Test
    public void testReactivateHealthProfessional() throws WorkFlowException {
        when(iActionService.reactiveRequest(actionRequestTo)).thenReturn("Reactivated");
        String result = actionController.reactivateHealthProfessional(actionRequestTo);
        assertEquals("Reactivated", result);
    }

    @Test
    public void testReactivationRecordsOfHealthProfessionalsToNmc() {
        when(iActionService.getReactivationRecordsOfHealthProfessionalsToNmc("1", "2", null, null, null)).thenReturn(reactivateHealthProfessionalResponseTO);
        ReactivateHealthProfessionalResponseTO result = actionController.reactivationRecordsOfHealthProfessionalsToNmc("1", "2", null, null, null);
        assertEquals(reactivateHealthProfessionalResponseTO, result);
    }

}*/
