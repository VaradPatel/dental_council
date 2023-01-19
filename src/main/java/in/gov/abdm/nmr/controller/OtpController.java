package in.gov.abdm.nmr.controller;
import javax.validation.Valid;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.IOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.exception.OtpException;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
@RequestMapping(NMRConstants.NOTIFICATION_REQUEST_MAPPING)
public class OtpController {

    @Autowired
	IOtpService otpService;

	/**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return Success/Failure
	 * @throws OtpException with message
	 */
	@PostMapping(path = NMRConstants.SEND_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo generateOtp(@Valid @RequestBody OtpGenerateRequestTo otpGenerateRequestTo)
			throws OtpException {
		return otpService.generateOtp(otpGenerateRequestTo);
	}

	/**
	 * API Endpoint to validate OTP
	 * @param otpValidateRequestTo coming from user
	 * @return Success/Failure
	 * @throws OtpException with message
	 */
	@PostMapping(path = NMRConstants.VERIFY_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public OtpValidateResponseTo validateOtp(@Valid @RequestBody OtpValidateRequestTo otpValidateRequestTo)
			throws OtpException {
		return otpService.validateOtp(otpValidateRequestTo);
	}
}
