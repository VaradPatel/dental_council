package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;

/**
 * Interface to declare reset password methods
 */
public interface IPasswordService {

    /**
     * Reset Password
     */
    ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo);

    /**
     * Change Password
     */
    ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo);

}
