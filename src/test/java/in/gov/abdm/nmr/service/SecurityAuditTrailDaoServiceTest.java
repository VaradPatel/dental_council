package in.gov.abdm.nmr.service;

import in.gov.abdm.nmr.entity.SecurityAuditTrail;
import in.gov.abdm.nmr.repository.ISecurityAuditTrailRepository;
import in.gov.abdm.nmr.service.impl.SecurityAuditTrailDaoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.CommonTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityAuditTrailDaoServiceTest {

    @InjectMocks
    SecurityAuditTrailDaoServiceImpl securityAuditTrailDaoService;
    @Mock
    private ISecurityAuditTrailRepository securityAuditTrailRepository;
    List<SecurityAuditTrail> securityAuditTrails;
    SecurityAuditTrail securityAuditTrail;

    @BeforeEach
    void setup() {
        securityAuditTrails = new ArrayList<>();
        securityAuditTrail = new SecurityAuditTrail();
        securityAuditTrail.setId(ID);
        securityAuditTrail.setIpAddress(IP_ADDRESS);
        securityAuditTrail.setUsername(TEST_USER);
        securityAuditTrails.add(securityAuditTrail);
    }

    @Test
    void testFindByUserAndCreatedAtGreaterThanEqualAndStatus() {
        when(securityAuditTrailRepository.findByUsernameAndCreatedAtGreaterThanEqualAndStatus(
                any(String.class), any(Timestamp.class), any(String.class))).thenReturn(securityAuditTrails);
        List<SecurityAuditTrail> byUserAndCreatedAtGreaterThanEqualAndStatus = securityAuditTrailDaoService.findByUserAndCreatedAtGreaterThanEqualAndStatus("1", CURRENT_TIMESTAMP, "1");
        assertEquals(ID, byUserAndCreatedAtGreaterThanEqualAndStatus.get(0).getId());
        assertEquals(IP_ADDRESS, byUserAndCreatedAtGreaterThanEqualAndStatus.get(0).getIpAddress());
        assertEquals(TEST_USER, byUserAndCreatedAtGreaterThanEqualAndStatus.get(0).getUsername());
    }

    @Test
    void testFindByCorrelationId() {
        when(securityAuditTrailRepository.findByCorrelationId(any(String.class))).thenReturn(securityAuditTrail);
        SecurityAuditTrail byCorrelationId = securityAuditTrailDaoService.findByCorrelationId("1");
        assertEquals(ID, byCorrelationId.getId());
        assertEquals(IP_ADDRESS, byCorrelationId.getIpAddress());
        assertEquals(TEST_USER, byCorrelationId.getUsername());
    }
}