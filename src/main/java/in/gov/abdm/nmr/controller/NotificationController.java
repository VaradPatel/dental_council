package in.gov.abdm.nmr.controller;
import java.security.NoSuchAlgorithmException;
import javax.validation.Valid;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import in.gov.abdm.nmr.util.NMRConstants;
import in.gov.abdm.nmr.exception.OtpException;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
public class NotificationController {

    @Autowired
    INotificationService otpNotificationService;

	/**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return Success/Failure
	 * @throws NoSuchAlgorithmException
	 * @throws OtpException
	 * @throws JsonProcessingException
	 */
	@PostMapping(path = NMRConstants.GENERATE_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo generateOtp(@Valid @RequestBody OtpGenerateRequestTo otpGenerateRequestTo)
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
