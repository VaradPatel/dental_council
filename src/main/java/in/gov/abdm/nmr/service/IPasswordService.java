package in.gov.abdm.nmr.service;
import java.security.GeneralSecurityException;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.InvalidRequestException;

/**
 * Interface to declare reset password methods
 */
public interface IPasswordService {

    ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) throws GeneralSecurityException, InvalidRequestException;

    ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) throws InvalidRequestException, GeneralSecurityException;

    ResponseMessageTo getResetPasswordLink(CreateHpUserAccountTo setPasswordLinkTo);

    ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo);

}
