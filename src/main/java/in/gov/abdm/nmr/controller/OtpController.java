package in.gov.abdm.nmr.controller;

import java.security.GeneralSecurityException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;

import in.gov.abdm.nmr.exception.TemplateException;
import in.gov.abdm.nmr.security.ChecksumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.abdm.nmr.dto.OTPResponseMessageTo;
import in.gov.abdm.nmr.dto.OtpGenerateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateRequestTo;
import in.gov.abdm.nmr.dto.OtpValidateResponseTo;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.service.IOtpService;
import in.gov.abdm.nmr.util.NMRConstants;

/**
 * Controller for sending and verifying one time password notifications.
 */
@RestController
@RequestMapping(NMRConstants.NOTIFICATION_REQUEST_MAPPING)
public class OtpController {

	private IOtpService otpService;
    
    private Validator validator;

	@Autowired
	ChecksumUtil checksumUtil;

	public OtpController(IOtpService otpService, Validator validator) {
        this.otpService = otpService;
        this.validator = validator;
    }

    /**
	 * API Endpoint to generate  OTP
	 * @param otpGenerateRequestTo coming from user
	 * @return Success/Failure
	 * @throws OtpException with message
	 */
	@PostMapping(path = NMRConstants.SEND_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public OTPResponseMessageTo generateOtp(@Valid @RequestBody OtpGenerateRequestTo otpGenerateRequestTo)
			throws OtpException, InvalidRequestException, TemplateException {
		checksumUtil.validateChecksum();
		return otpService.generateOtp(otpGenerateRequestTo);
	}

	/**
	 * API Endpoint to validate OTP
	 * @param otpValidateRequestTo coming from user
	 * @return Success/Failure
	 * @throws OtpException with message
	 */
	@PostMapping(path = NMRConstants.VERIFY_OTP, produces = MediaType.APPLICATION_JSON_VALUE)
	public OtpValidateResponseTo validateOtp(@RequestBody OtpValidateRequestTo otpValidateRequestTo)
			throws OtpException, GeneralSecurityException {
		checksumUtil.validateChecksum();
        if (otpService.isOtpEnabled()) {
            Set<ConstraintViolation<OtpValidateRequestTo>> constraintViolations = validator.validate(otpValidateRequestTo);
            if (!constraintViolations.isEmpty()) {
                throw new ConstraintViolationException(constraintViolations);
            }
        }
		return otpService.validateOtp(otpValidateRequestTo, false);
	}
	
    @GetMapping("/otp-enabled")
    public boolean isOtpEnabled() {
        return otpService.isOtpEnabled();
    }
}
