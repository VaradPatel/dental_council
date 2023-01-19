package in.gov.abdm.nmr.service.impl;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.PasswordResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Service layer to perform reset password operations
 */
@Service
public class ResetPasswordService implements IResetPasswordService {

    @Value("${council.reset-password.url}")
    private String resetPasswordUrl;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    IUserDaoService userDaoService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    INotificationService notificationService;

    /**
     * Creates new unique token for reset password transaction
     *
     * @param setPasswordLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getResetPasswordLink(GetSetPasswordLinkTo setPasswordLinkTo) {

        String token = RandomString.make(30);

        try {

            passwordResetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));
            if (userRepository.existsByUsername(setPasswordLinkTo.getContact())) {

                PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserName(setPasswordLinkTo.getContact());

                if (passwordResetToken != null) {
                    passwordResetToken.setToken(token);
                } else {
                    passwordResetToken = new PasswordResetToken(token, setPasswordLinkTo.getContact());
                }
                passwordResetTokenRepository.save(passwordResetToken);

                String resetPasswordLink = resetPasswordUrl + "/" + token;

                System.out.println(resetPasswordLink);

                return notificationService.sendNotificationForResetPasswordLink(setPasswordLinkTo.getType(),setPasswordLinkTo.getContact(),resetPasswordLink);

            } else {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }

    /**
     * find user by unique token
     *
     * @param newPasswordTo unique token and new password
     * @return user object
     */
    @Override
    public ResponseMessageTo setNewPassword(SetNewPasswordTo newPasswordTo) {
        try {

            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(newPasswordTo.getToken());

            User user = userDaoService.findUserDetailByUsername(passwordResetToken.getUserName());

            if(null==user){
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }
            if (passwordResetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {

                return new ResponseMessageTo(NMRConstants.LINK_EXPIRED);
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(newPasswordTo.getPassword());
            user.setPassword(encodedPassword);
            userRepository.save(user);

            return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }
}

