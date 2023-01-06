package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 * Controller for resetting and changing user password
 */
@RestController
public class PasswordController {

    @Autowired
	IPasswordService passwordService;

	/**
	 * API Endpoint to reset password
	 * @param resetPasswordRequestTo coming from user
	 * @return Response message Success/Fail
	 */
	@PostMapping(path = NMRConstants.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo resetPassword(@Valid @RequestBody ResetPasswordRequestTo resetPasswordRequestTo) {
		return passwordService.resetPassword(resetPasswordRequestTo);
	}

	/**
	 * API Endpoint to change password
	 * @param changePasswordRequestTo coming from user
	 * @return Response message Success/Fail
	 */
	@PostMapping(path = NMRConstants.CHANGE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseMessageTo changePassword(@Valid @RequestBody ChangePasswordRequestTo changePasswordRequestTo) {
		return passwordService.changePassword(changePasswordRequestTo);
	}
}
