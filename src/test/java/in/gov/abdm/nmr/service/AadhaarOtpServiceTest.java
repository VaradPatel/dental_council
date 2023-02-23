package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.client.AadhaarFClient;
import in.gov.abdm.nmr.service.impl.AadhaarOtpServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
class AadhaarOtpServiceTest {

    @InjectMocks
    AadhaarOtpServiceImpl aadhaarOtpService;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    RestTemplate restTemplateMock;

    @Mock
    AadhaarFClient aadhaarFClient;


}
