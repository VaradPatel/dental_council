package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for resetting user password
 */
@RestController
public class ResetPasswordController {

    @Autowired
	IResetPasswordService resetPasswordService;

	/**
	 * API Endpoint to reset password
	 * @param resetPasswordRequestTo coming from user
	 * @return ResetPasswordResponseTo object
	 */
	@PostMapping(path = NMRConstants.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo resetPassword(@Valid @RequestBody ResetPasswordRequestTo resetPasswordRequestTo) {
		return resetPasswordService.resetPassword(resetPasswordRequestTo);
	}
}
