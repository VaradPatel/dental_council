package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.Password;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.redis.hash.Otp;
import in.gov.abdm.nmr.repository.IPasswordRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.impl.AccessControlServiceImpl;
import in.gov.abdm.nmr.service.impl.PasswordDaoServiceImpl;
import in.gov.abdm.nmr.util.TestAuthentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PasswordDaoServiceTest {

    @InjectMocks
    PasswordDaoServiceImpl passwordDaoService;
    @Mock
    private IPasswordRepository passwordRepository;


    @BeforeEach
    void setup() {
    }

    @Test
    void testGetLoggedInUser() {
        when(passwordRepository.save(any(Password.class))).thenReturn(getPassword());
        passwordDaoService.save(getPassword());
        verify(passwordRepository, times(1)).save(passwordRepository.save(any(Password.class)));
    }

}