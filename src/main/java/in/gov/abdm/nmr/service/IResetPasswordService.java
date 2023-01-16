package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.dto.GetSetPasswordLinkTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;
import in.gov.abdm.nmr.entity.User;
import javax.servlet.http.HttpServletRequest;

/**
 * Interface to declare reset password methods
 */
public interface IResetPasswordService {

    ResponseMessageTo getResetPasswordLink(GetSetPasswordLinkTo setPasswordLinkTo);

    ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo);

    void sendNotification(GetSetPasswordLinkTo setPasswordLinkTo, String link);
}
