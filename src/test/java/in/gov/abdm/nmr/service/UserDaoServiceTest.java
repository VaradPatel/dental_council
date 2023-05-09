package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.NotificationToggleResponseTO;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserType;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.impl.OtpServiceImpl;
import in.gov.abdm.nmr.service.impl.UserDaoServiceImpl;
import in.gov.abdm.nmr.service.impl.UserServiceImpl;
import in.gov.abdm.nmr.util.NMRConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

}