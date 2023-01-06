package in.gov.abdm.nmr.service;
import in.gov.abdm.nmr.entity.User;
import javax.servlet.http.HttpServletRequest;

/**
 * Interface to declare reset password methods
 */
public interface IResetPasswordService {

    boolean createPasswordResetTokenForUser(String email,String token);

    User getUserByPasswordResetToken(String token);

    void updatePassword(User user, String newPassword);

    String getSiteURL(HttpServletRequest request);

    void sendEmail(String email, String link);
}
