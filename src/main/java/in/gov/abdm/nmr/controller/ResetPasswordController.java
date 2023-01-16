package in.gov.abdm.nmr.controller;

import in.gov.abdm.nmr.dto.GetSetPasswordLinkTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller to perform reset password operations
 */
@RestController
public class ResetPasswordController {

    @Autowired
    private IResetPasswordService resetPasswordService;

    /**
     * Sends reset password link on email/mobile
     * @param setPasswordLinkTo receiver email/mobile
     * @return Success/Fail message
     */
    @PostMapping(NMRConstants.GET_SET_PASSWORD_LINK)
    public ResponseMessageTo getResetPasswordLink(@RequestBody GetSetPasswordLinkTo setPasswordLinkTo) {

        return resetPasswordService.getResetPasswordLink(setPasswordLinkTo);

    }

    /**
     * Performs reset password operation
     * @param newPasswordTo to set new password
     * @return reset password page with success/failure
     */
    @PostMapping(NMRConstants.SET_NEW_PASSWORD)
    public ResponseMessageTo processResetPassword(@RequestBody SetNewPasswordTo newPasswordTo) {

        return resetPasswordService.setNewPassword(newPasswordTo);
    }
}