package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.KeyValue;
import in.gov.abdm.nmr.dto.NotificationRequestTo;
import in.gov.abdm.nmr.entity.PasswordResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer to perform reset password operations
 */
@Service
@Transactional
public class ResetPasswordService implements IResetPasswordService {

    @Autowired
    NotificationFClient notificationFClient;

    @Value("${notification.origin}")
    private String notificationOrigin;

    @Value("${notification.sender}")
    private String notificationSender;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    /**
     * Creates new unique token for reset password transaction
     * @param email username of user
     * @param token unique generated token
     * @return returns boolean result of token creation
     */
    @Override
    public boolean createPasswordResetTokenForUser(String email, String token) {
        passwordResetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

        if (userRepository.existsByUsername(email)) {

            PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserName(email);

            if (passwordResetToken != null) {
                passwordResetToken.setToken(token);
            } else {
                passwordResetToken = new PasswordResetToken(token, email);
            }
            passwordResetTokenRepository.save(passwordResetToken);
            return true;
        } else {
            return false;
        }
    }

    /**
     * find user by unique token
     * @param token unique token
     * @return user object
     */
    @Override
    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        User user = userRepository.findByUsername(passwordResetToken.getUserName());
        if (passwordResetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {

            user = null;
        }
        return user;
    }

    /**
     * Updates user password in repository
     * @param user user object which need to save
     * @param newPassword newly created password by user
     */
    @Override
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }

    /**
     * Gets context URL
     * @param request Request to find context url
     * @return context URL String
     */
    public String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    /**
     * Calls send email API
     * @param email receiver email id
     * @param link reset password page link
     */
    @Override
    public void sendEmail(String email, String link) {

        String content = "Hello,\n"
                + "You have requested to reset your password."
                + "Click the link below to change your password:\n"
                + "\n \"" + link + "\""
                + "\n"
                + "\nIgnore this email if you do remember your password, "
                + "or you have not made the request.";

        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(NMRConstants.OTP_CONTENT_TYPE);

        notificationRequestTo.setType(List.of(NotificationType.EMAIL.getNotificationType()));
        KeyValue receiver = new KeyValue();
        receiver.setKey(NMRConstants.EMAIL_ID);
        receiver.setValue(email);
        notificationRequestTo.setReceiver(List.of(receiver));

        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(NMRConstants.EMAIL_TEMPLATE_ID);
        KeyValue subjectKeyValue = new KeyValue();
        subjectKeyValue.setKey(NMRConstants.SUBJECT);
        subjectKeyValue.setValue(NMRConstants.RESET_PASS_EMAIL_SUBJECT);
        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(content);
        notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));

        notificationFClient.sendNotification(notificationRequestTo, Timestamp.valueOf(LocalDateTime.now()), "123");

    }
}

