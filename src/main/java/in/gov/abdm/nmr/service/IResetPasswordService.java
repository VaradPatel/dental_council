package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResetPasswordResponseTo;

/**
 * Interface to declare reset password methods
 */
public interface IResetPasswordService {

    /**
     * Reset Password
     */
    ResetPasswordResponseTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo);

}
