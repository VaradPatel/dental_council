package in.gov.abdm.nmr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.dto.AadhaarAuthOtpTo;
import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import in.gov.abdm.nmr.service.impl.AadhaarOtpServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @Disabled
    void testSendOtpShouldSendOtp() throws JsonProcessingException {

        AadhaarOtpGenerateRequestTo aadhaarOtpGenerateRequestTo = new AadhaarOtpGenerateRequestTo();
        aadhaarOtpGenerateRequestTo.setAadhaarNumber("123456780000");

        AadhaarAuthOtpTo aadhaarAuthOtpTo = new AadhaarAuthOtpTo();
        aadhaarAuthOtpTo.setCode("123");
        AadhaarResponseTo aadhaarResponseTo = new AadhaarResponseTo();
        aadhaarResponseTo.setResponse("Some response");
        aadhaarResponseTo.setDeviceType("mobile");
        aadhaarResponseTo.setAadhaarAuthOtpDto(aadhaarAuthOtpTo);
        when(restTemplateMock.postForEntity(anyString(),any(), any())).thenReturn(ResponseEntity.ok(aadhaarResponseTo));
        when(objectMapper.writeValueAsString(any())).thenReturn("json string");
        AadhaarResponseTo aadhaarResponseTo1 = aadhaarOtpService.sendOtp(aadhaarOtpGenerateRequestTo);
        assertEquals("123", aadhaarResponseTo1.getAadhaarAuthOtpDto().getCode());

    }

}
