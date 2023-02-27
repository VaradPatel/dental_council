package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import in.gov.abdm.nmr.service.AadhaarOtpService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
@RequestMapping(NMRConstants.AADHAAR_REQUEST_MAPPING)
public class AadhaarOtpController {

    @Autowired
	AadhaarOtpService aadharOtpService;

	/**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return AadhaarResponseDto object
	 */
	@PostMapping(path = NMRConstants.SEND_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public AadhaarResponseTo sendOtp(@Valid @RequestBody AadhaarOtpGenerateRequestTo otpGenerateRequestTo) {
		return aadharOtpService.sendOtp(otpGenerateRequestTo);
	}

	/**
	 * API Endpoint to validate OTP
	 * @param otpValidateRequestTo coming from user
	 * @return AadhaarResponseDto object
	 */
	@PostMapping(path = NMRConstants.VERIFY_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public AadhaarResponseTo verifyOtp(@Valid @RequestBody AadhaarOtpValidateRequestTo otpValidateRequestTo){
		return aadharOtpService.verifyOtp(otpValidateRequestTo);
	}
}
