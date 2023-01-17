package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.client.NotificationDBFClient;
import in.gov.abdm.nmr.client.NotificationFClient;
import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.PasswordResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.enums.NotificationType;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.service.IResetPasswordService;
import in.gov.abdm.nmr.util.NMRConstants;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Service layer to perform reset password operations
 */
@Service
@Transactional
public class ResetPasswordService implements IResetPasswordService {

    @Autowired
    NotificationFClient notificationFClient;

    @Autowired
    NotificationDBFClient notificationDBFClient;

    @Value("${notification.origin}")
    private String notificationOrigin;

    @Value("${notification.sender}")
    private String notificationSender;

    @Value("${council.reset-password.url}")
    private String resetPasswordUrl;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    MessageSource messageSource;

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
                return sendNotification(setPasswordLinkTo, resetPasswordLink);

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
            User user = userRepository.findByUsername(passwordResetToken.getUserName());
            if (passwordResetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {

                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
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

    public NotificationDBFClient getNotificationDBFClient() {
        return notificationDBFClient;
    }

    /**
     * Calls send email API
     *
     * @param setPasswordLinkTo receiver details
     * @param link              reset password page link
     */
    @Override
    public ResponseMessageTo sendNotification(GetSetPasswordLinkTo setPasswordLinkTo, String link) {

        Template template;
        try {
            String templateId = messageSource.getMessage(NMRConstants.OTP_MESSAGES_PROPERTIES_KEY.toLowerCase(), null, Locale.ENGLISH);
            template = notificationDBFClient.getTemplateById(new BigInteger(templateId));

        } catch (NoSuchMessageException noSuchMessageException) {
            return new ResponseMessageTo(NMRConstants.TEMPLATE_ID_NOT_FOUND_IN_PROPERTIES);
        }

        if (null == template) {
            return new ResponseMessageTo(NMRConstants.TEMPLATE_NOT_FOUND);
        }

        NotificationRequestTo notificationRequestTo = new NotificationRequestTo();
        notificationRequestTo.setOrigin(notificationOrigin);
        notificationRequestTo.setSender(notificationSender);
        notificationRequestTo.setContentType(NMRConstants.OTP_CONTENT_TYPE);

        KeyValue receiver = new KeyValue();

        if (setPasswordLinkTo.getType().equalsIgnoreCase(NMRConstants.MOBILE)) {
            notificationRequestTo.setType(List.of(NotificationType.SMS.getNotificationType()));
            receiver.setKey(NMRConstants.MOBILE);
        } else {
            notificationRequestTo.setType(List.of(NotificationType.EMAIL.getNotificationType()));
            receiver.setKey(NMRConstants.EMAIL_ID);
        }
        receiver.setValue(setPasswordLinkTo.getContact());
        notificationRequestTo.setReceiver(List.of(receiver));

        KeyValue templateKeyValue = new KeyValue();
        templateKeyValue.setKey(NMRConstants.TEMPLATE_ID);
        templateKeyValue.setValue(template.getId().toString());
        KeyValue subjectKeyValue = new KeyValue();
        subjectKeyValue.setKey(NMRConstants.SUBJECT);
        subjectKeyValue.setValue(NMRConstants.SET_PASSWORD_EMAIlL_SUBJECT);
        KeyValue contentKeyValue = new KeyValue();
        contentKeyValue.setKey(NMRConstants.CONTENT);
        contentKeyValue.setValue(link);
        notificationRequestTo.setNotification(List.of(templateKeyValue, subjectKeyValue, contentKeyValue));

        NotificationResponseTo response;
        try {
            response = notificationFClient.sendNotification(notificationRequestTo, Timestamp.valueOf(LocalDateTime.now()), "123");

        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
        return new ResponseMessageTo(response.status().equalsIgnoreCase(NMRConstants.SENT_RESPONSE) ? NMRConstants.SUCCESS_RESPONSE : NMRConstants.FAILURE_RESPONSE);

    }
}

