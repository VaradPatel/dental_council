package in.gov.abdm.nmr.service;
import java.security.GeneralSecurityException;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.OtpException;

/**
 * Interface to declare reset password methods
 */
public interface IPasswordService {

    ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) throws GeneralSecurityException, InvalidRequestException, OtpException;

    ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) throws InvalidRequestException, GeneralSecurityException;

    ResponseMessageTo getResetPasswordLink(SendLinkOnMailTo sendLinkOnMailTo);

    ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo);

}
