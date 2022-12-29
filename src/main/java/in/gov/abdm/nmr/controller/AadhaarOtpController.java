package in.gov.abdm.nmr.controller;

import javax.validation.Valid;

import in.gov.abdm.nmr.dto.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.AadhaarResponseTo;
import in.gov.abdm.nmr.service.AadhaarOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.dto.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.exception.OtpException;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
public class AadhaarOtpController {

    @Autowired
	AadhaarOtpService aadharOtpService;

	/**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return AadhaarResponseDto object
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = NMRConstants.GENERATE_AADHAR_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public AadhaarResponseTo sendOtp(@Valid @RequestBody AadhaarOtpGenerateRequestTo otpGenerateRequestTo)
			throws  JsonProcessingException {
		return aadharOtpService.sendOtp(otpGenerateRequestTo);
	}

	/**
	 * API Endpoint to validate OTP
	 * @param otpValidateRequestTo coming from user
	 * @return AadhaarResponseDto object
	 * @throws OtpException
	 */
	@PostMapping(path = NMRConstants.VALIDATE_AADHAR_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public AadhaarResponseTo verifyOtp(@Valid @RequestBody AadhaarOtpValidateRequestTo otpValidateRequestTo) throws JsonProcessingException {
		return aadharOtpService.verifyOtp(otpValidateRequestTo);
	}
}
