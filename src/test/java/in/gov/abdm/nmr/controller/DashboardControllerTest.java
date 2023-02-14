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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testFetchCountOnCard() throws InvalidRequestException, AccessDeniedException {
        FetchCountOnCardResponseTO expected = new FetchCountOnCardResponseTO();
        when(iFetchCountOnCardService.fetchCountOnCard()).thenReturn(expected);
        FetchCountOnCardResponseTO result = dashboardController.fetchCountOnCard();
        assertEquals(expected, result);
    }


    @Test
    public void testDashBoardResponseTO() throws InvalidRequestException {
        DashboardRequestTO requestTO = new DashboardRequestTO();
        DashboardResponseTO expectedResponseTO = new DashboardResponseTO();
        when(iFetchSpecificDetailsService.fetchDashboardData(requestTO)).thenReturn(expectedResponseTO);
        DashboardResponseTO actualResponseTO = dashboardController.DashBoardResponseTO(requestTO);
        Assertions.assertEquals(expectedResponseTO, actualResponseTO);
    }
}




