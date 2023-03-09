package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.*;

/**
 * Interface to declare reset password methods
 */
public interface IPasswordService {

    ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo);

    ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo);

    ResponseMessageTo getResetPasswordLink(CreateHpUserAccountTo setPasswordLinkTo);

    ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo);

}
