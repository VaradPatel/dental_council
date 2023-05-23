package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDaoServiceTest {

    @InjectMocks
    UserDaoServiceImpl userDaoService;
    @Mock
    private IUserRepository userDetailRepository;
    @Mock
    private ISmcProfileRepository smcProfileRepository;
    @Mock
    private INmcProfileRepository nmcProfileRepository;
    @Mock
    private INbeProfileRepository nbeProfileRepository;
    @Mock
    private EntityManager entityManager;

    @Test
    void testFindById() {
        when(userDetailRepository.findById(any(BigInteger.class))).thenReturn(Optional.of(getUser(UserTypeEnum.SMC.getId())));
        User user = userDaoService.findById(ID);
        assertEquals(ID, user.getId());
        assertEquals(EMAIL_ID, user.getEmail());
        assertEquals(true, user.isAccountNonLocked());
        assertEquals(0, user.getFailedAttempt());
    }

    @Test
    void testUnlockUserShouldUnlockUser(){
        doNothing().when(userDetailRepository).unlockUser(any(BigInteger.class));
        userDaoService.unlockUser(BigInteger.valueOf(1));
        verify(userDetailRepository, times(1)).unlockUser(any(BigInteger.class));
    }

}