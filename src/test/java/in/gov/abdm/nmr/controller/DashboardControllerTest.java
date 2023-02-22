package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.DashboardRequestTO;
import in.gov.abdm.nmr.dto.DashboardResponseTO;
import in.gov.abdm.nmr.dto.FetchCountOnCardResponseTO;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.service.IFetchCountOnCardService;
import in.gov.abdm.nmr.service.IFetchSpecificDetailsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    @InjectMocks
    DashboardController dashboardController;
    @Mock
    IFetchCountOnCardService iFetchCountOnCardService;
    @Mock
    IFetchSpecificDetailsService iFetchSpecificDetailsService;

    FetchCountOnCardResponseTO expected;
    DashboardRequestTO requestTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        expected = new FetchCountOnCardResponseTO();
        requestTO = new DashboardRequestTO();
    }

    @AfterEach
    void tearDown() {
        expected = null;
        requestTO = null;
    }

    @Test
    void testFetchCountOnCard() throws InvalidRequestException, AccessDeniedException {
        when(iFetchCountOnCardService.fetchCountOnCard()).thenReturn(expected);
        FetchCountOnCardResponseTO result = dashboardController.fetchCountOnCard();
        assertEquals(expected, result);
    }


    @Test
    void testDashBoardResponseTO() throws InvalidRequestException {
        DashboardResponseTO expectedResponseTO = new DashboardResponseTO();
        when(iFetchSpecificDetailsService.fetchDashboardData(requestTO)).thenReturn(expectedResponseTO);
        DashboardResponseTO actualResponseTO = dashboardController.DashBoardResponseTO(requestTO);
        Assertions.assertEquals(expectedResponseTO, actualResponseTO);
    }
}




