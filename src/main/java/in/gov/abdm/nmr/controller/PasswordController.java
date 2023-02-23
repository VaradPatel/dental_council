package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Controller for resetting and changing user password
 */
@RestController
@RequestMapping(NMRConstants.USER_REQUEST_MAPPING)
public class PasswordController {

    @Autowired
    IPasswordService passwordService;


    /**
     * Sends reset password link on email/mobile
     *
     * @param setPasswordLinkTo receiver email/mobile
     * @return Success/Fail message
     */
    @PostMapping(NMRConstants.PASSWORD_LINK)
    public ResponseMessageTo getResetPasswordLink(@RequestBody GetSetPasswordLinkTo setPasswordLinkTo) {

        return passwordService.getResetPasswordLink(setPasswordLinkTo);

    }

    /**
     * Performs reset password operation
     *
     * @param newPasswordTo to set new password
     * @return reset password page with success/failure
     */
    @PostMapping(NMRConstants.SET_PASSWORD)
    public ResponseMessageTo setNewPassword(@RequestBody SetNewPasswordTo newPasswordTo) {

        return passwordService.setNewPassword(newPasswordTo);
    }

    /**
     * API Endpoint to reset password
     *
     * @param resetPasswordRequestTo coming from user
     * @return Response message Success/Fail
     */
    @PostMapping(path = NMRConstants.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo resetPassword(@Valid @RequestBody ResetPasswordRequestTo resetPasswordRequestTo) {
        return passwordService.resetPassword(resetPasswordRequestTo);
    }

    /**
     * API Endpoint to change password
     *
     * @param changePasswordRequestTo coming from user
     * @return Response message Success/Fail
     */
    @PostMapping(path = NMRConstants.CHANGE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo changePassword(@Valid @RequestBody ChangePasswordRequestTo changePasswordRequestTo) {
        return passwordService.changePassword(changePasswordRequestTo);
    }
}
