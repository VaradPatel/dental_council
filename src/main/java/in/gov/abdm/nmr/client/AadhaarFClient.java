package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static in.gov.abdm.nmr.util.NMRConstants.*;

/**
 * Client to call Aadhaar Service APIS
 */
@FeignClient(name = AADHAAR_SERVICE, url = GLOBAL_AADHAAR_ENDPOINT)
public interface AadhaarFClient {

    @PostMapping(value = AADHAR_SERVICE_SEND_OTP)
    AadhaarResponseTo sendOTP(@RequestBody AadhaarOtpGenerateRequestTo otpGenerateRequestTo);

    @PostMapping(value = AADHAR_SERVICE_VERIFY_OTP)
    AadhaarResponseTo verifyOTP(@RequestBody AadhaarOtpValidateRequestTo otpValidateRequestTo);

}