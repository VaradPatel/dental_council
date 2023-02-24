package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.LoginRequestTO;
import in.gov.abdm.nmr.dto.LoginResponseTO;
import in.gov.abdm.nmr.service.IAuthService;
import in.gov.abdm.nmr.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @InjectMocks
    private AuthController authController;
    @Mock
    private IAuthService authService;
    LoginResponseTO expectedResponse;
    LoginRequestTO loginRequestTO;
    HttpServletResponse response;

    @BeforeEach
    public void init() {
        expectedResponse = new LoginResponseTO();
        loginRequestTO = new LoginRequestTO();
        loginRequestTO.setUsername("user");
        loginRequestTO.setPassword("password");
        response = mock(HttpServletResponse.class);
    }

    @AfterEach
    void tearDown() {
        expectedResponse = null;
        loginRequestTO = null;
    }

    @Test
    void testLoginSuccessfulAuth() {
        AuthServiceImpl service = mock(AuthServiceImpl.class);
        when(service.successfulAuth(response)).thenReturn(expectedResponse);
        AuthController loginController = new AuthController(service);
        LoginResponseTO actualResponse = loginController.login(loginRequestTO, response);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testRefreshToken_Success() throws Exception {
        when(authService.successfulAuth(response)).thenReturn(expectedResponse);
        LoginResponseTO actualResponse = authController.refreshToken(response);
        verify(authService).successfulAuth(response);
        assertEquals(expectedResponse, actualResponse);
    }
}