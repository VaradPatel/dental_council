package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.GetSetPasswordLinkTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;

/**
 * Interface to declare reset password methods
 */
public interface IResetPasswordService {

    ResponseMessageTo getResetPasswordLink(GetSetPasswordLinkTo setPasswordLinkTo);

    ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo);

    ResponseMessageTo sendNotification(GetSetPasswordLinkTo setPasswordLinkTo, String link);
}
