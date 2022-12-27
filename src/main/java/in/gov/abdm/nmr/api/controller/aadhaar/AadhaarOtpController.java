package in.gov.abdm.nmr.api.controller.aadhaar;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarResponseTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpGenerateRequestTo;
import in.gov.abdm.nmr.api.controller.aadhaar.to.AadhaarOtpValidateRequestTo;
import in.gov.abdm.nmr.api.exception.OtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

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
