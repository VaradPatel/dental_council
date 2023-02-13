package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.client.AadhaarFClient;
import in.gov.abdm.nmr.dto.AadhaarAuthOtpTo;
import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import in.gov.abdm.nmr.service.impl.AadhaarOtpServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
