package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.impl.AccessControlServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessControlServiceTest {

    @InjectMocks
    AccessControlServiceImpl accessControlService;
    @Mock
    private IUserRepository userRepository;
    User loggedInUser;

    @BeforeEach
    void setup() {
        loggedInUser = getUser(UserTypeEnum.HEALTH_PROFESSIONAL.getId());
    }

    @Test
    void testValidateUser() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userRepository.findByUsername(any(String.class), any(BigInteger.class))).thenReturn(loggedInUser);
        accessControlService.validateUser(ID);
        Mockito.verify(userRepository, Mockito.times(1)).findByUsername(anyString(), any(BigInteger.class));
    }

    @Test
    void testValidateUserShouldThrowInvalidUserException() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userRepository.findByUsername(any(String.class), any(BigInteger.class))).thenReturn(null);
        assertThrows(AccessDeniedException.class, () -> accessControlService.validateUser(ID));
    }

    @Test
    void testGetLoggedInUser() {
        SecurityContextHolder.getContext().setAuthentication(new TestAuthentication());
        when(userRepository.findByUsername(any(String.class), any(BigInteger.class))).thenReturn(loggedInUser);
        User loggedInUser = accessControlService.getLoggedInUser();
        assertEquals(ID, loggedInUser.getId());
        assertEquals(EMAIL_ID, loggedInUser.getEmail());
        assertEquals(true, loggedInUser.isAccountNonLocked());
        assertEquals(ID, loggedInUser.getGroup().getId());
        assertEquals(EMAIL_ID, loggedInUser.getEmail());
        assertEquals(HPR_ID, loggedInUser.getHprId());
        assertEquals(NMR_ID, loggedInUser.getNmrId());
        assertEquals(0, loggedInUser.getFailedAttempt());
    }
}