package in.gov.abdm.nmr.api.controller.notification.otp;

import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.api.constant.NMRConstants;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpGenerateRequestTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpGenerateResponseTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpValidateRequestTo;
import in.gov.abdm.nmr.api.controller.notification.otp.to.OtpValidateResponseTo;
import in.gov.abdm.nmr.api.exception.OtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
public class OtpNotificationController {

    @Autowired
    OtpNotificationService otpNotificationService;

	/**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return Success/Failure
	 * @throws NoSuchAlgorithmException
	 * @throws OtpException
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = NMRConstants.GENERATE_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public OtpGenerateResponseTo generateOtp(@Valid @RequestBody OtpGenerateRequestTo otpGenerateRequestTo)
			throws NoSuchAlgorithmException, OtpException, JsonProcessingException {
		return otpNotificationService.generateOtp(otpGenerateRequestTo);
	}

	/**
	 * API Endpoint to validate OTP
	 * @param otpValidateRequestTo coming from user
	 * @return Success/Failure
	 * @throws OtpException
	 */
	@PostMapping(path = NMRConstants.VALIDATE_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public OtpValidateResponseTo validateOtp(@Valid @RequestBody OtpValidateRequestTo otpValidateRequestTo)
			throws OtpException {
		return otpNotificationService.validateOtp(otpValidateRequestTo);
	}
}
