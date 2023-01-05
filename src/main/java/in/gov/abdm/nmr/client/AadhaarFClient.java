package in.gov.abdm.nmr.client;

import in.gov.abdm.nmr.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import static in.gov.abdm.nmr.util.NMRConstants.GLOBAL_AADHAAR_ENDPOINT;
import static in.gov.abdm.nmr.util.NMRConstants.AADHAAR_SERVICE;
import static in.gov.abdm.nmr.util.NMRConstants.AADHAR_SERVICE_SEND_OTP;
import static in.gov.abdm.nmr.util.NMRConstants.AADHAR_SERVICE_VERIFY_OTP;

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