package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.security.ChecksumUtil;
import in.gov.abdm.nmr.security.common.ProtectedPaths;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.GeneralSecurityException;

import javax.validation.Valid;

/**
 * Controller for resetting and changing user password
 */
@RestController
public class PasswordController {

    @Autowired
    IPasswordService passwordService;

    @Autowired
    ChecksumUtil checksumUtil;

    /**
     * Performs reset password operation
     *
     * @param newPasswordTo to set new password
     * @return reset password page with success/failure
     */
    @PostMapping(NMRConstants.SET_PASSWORD)
    public ResponseMessageTo setNewPassword(@RequestBody SetNewPasswordTo newPasswordTo) {

        checksumUtil.validateChecksum();
        return passwordService.setNewPassword(newPasswordTo);
    }

    /**
     * API Endpoint to reset password
     *
     * @param resetPasswordRequestTo coming from user
     * @return Response message Success/Fail
     * @throws GeneralSecurityException 
     * @throws InvalidRequestException 
     */
    @PostMapping(path = NMRConstants.RESET_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseMessageTo resetPassword(@Valid @RequestBody ResetPasswordRequestTo resetPasswordRequestTo) throws InvalidRequestException, GeneralSecurityException, OtpException {
        checksumUtil.validateChecksum();
        return passwordService.resetPassword(resetPasswordRequestTo);
    }

    /**
     * API Endpoint to change password
     *
     * @param changePasswordRequestTo coming from user
     * @return Response message Success/Fail
     * @throws InvalidRequestException 
     * @throws GeneralSecurityException 
     */
    @PostMapping(path = ProtectedPaths.CHANGE_PASSWORD, produces = MediaType.APPLICATION_JSON_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseMessageTo changePassword(@Valid @RequestBody ChangePasswordRequestTo changePasswordRequestTo) throws InvalidRequestException, GeneralSecurityException {
        checksumUtil.validateChecksum();
        return passwordService.changePassword(changePasswordRequestTo);
    }
}
