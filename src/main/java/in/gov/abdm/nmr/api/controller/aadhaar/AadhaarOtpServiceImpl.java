package in.gov.abdm.nmr.api.controller.aadhaar;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarResponseTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpValidateRequestTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementations of methods to send and validate Aadhaar OTP
 */
@Service
@Transactional
public class AadhaarOtpServiceImpl implements AadhaarOtpService {

    @Autowired
    RestTemplate restTemplateDisableSSL;

    @Value("${global.aadhaar.endpoint}")
    private String aadharEndpoint;

    @Autowired
    ObjectMapper mapper;
    /**
     * Sends Aadhar OTP
     * @param otpGenerateRequestTo coming from Aadhaar OTP Service
     * @return AadhaarResponseDto Object
     * @throws JsonProcessingException
     */
    @Override
    public AadhaarResponseTo sendOtp(AadhaarOtpGenerateRequestTo otpGenerateRequestTo) throws JsonProcessingException {

        String jsonString = mapper.writeValueAsString(otpGenerateRequestTo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(NMRConstants.TIMESTAMP, Timestamp.valueOf(LocalDateTime.now()).toString());
        HttpEntity<String> dscRequest = new HttpEntity<String>(jsonString, headers);
        ResponseEntity<AadhaarResponseTo> response= restTemplateDisableSSL.postForEntity(aadharEndpoint+NMRConstants.AADHAR_SERVICE_SEND_OTP, dscRequest, AadhaarResponseTo.class);
        return response.getBody();
    }

    /**
     * Verifies Aadhaar OTP
     * @param otpValidateRequestTo coming from Aadhaar OTP Service
     * @return AadhaarResponseDto Object
     * @throws JsonProcessingException
     */
    @Override
    public AadhaarResponseTo verifyOtp(AadhaarOtpValidateRequestTo otpValidateRequestTo) throws JsonProcessingException {

        String jsonString = mapper.writeValueAsString(otpValidateRequestTo);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(NMRConstants.TIMESTAMP, Timestamp.valueOf(LocalDateTime.now()).toString());
        HttpEntity<String> dscRequest = new HttpEntity<String>(jsonString, headers);
        ResponseEntity<AadhaarResponseTo> response= restTemplateDisableSSL.postForEntity(aadharEndpoint+NMRConstants.AADHAR_SERVICE_VERIFY_OTP, dscRequest, AadhaarResponseTo.class);
        return response.getBody();
    }
}

