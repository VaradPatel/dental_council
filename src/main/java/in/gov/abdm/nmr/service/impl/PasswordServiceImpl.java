package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementations of methods for resetting and changing password
 */
@Service
@Transactional
public class PasswordServiceImpl implements IPasswordService {

    @Autowired
    IUserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${council.reset-password.url}")
    private String resetPasswordUrl;

    @Autowired
    IUserDaoService userDaoService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    INotificationService notificationService;

    @Autowired
    private EntityManager entityManager;

    /**
     * Creates new unique token for reset password transaction
     *
     * @param setPasswordLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getResetPasswordLink(GetSetPasswordLinkTo setPasswordLinkTo) {
        try {
            if (!userRepository.existsByUsername(setPasswordLinkTo.getUsername())) {
                User userDetail = new User(null, setPasswordLinkTo.getUsername(), null, null, true, true, //
                        entityManager.getReference(UserType.class, UserTypeEnum.HEALTH_PROFESSIONAL.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserGroup.class, Group.HEALTH_PROFESSIONAL.getId()), true, 0, null);
                userDaoService.saveUserDetail(userDetail);

                passwordResetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

                if (userRepository.existsByUsername(setPasswordLinkTo.getUsername())) {
                    return passwordReset(setPasswordLinkTo);
                } else {
                    return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.USER_NAME_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }

    public ResponseMessageTo passwordReset(GetSetPasswordLinkTo setPasswordLinkTo) {
        String token = RandomString.make(30);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserName(setPasswordLinkTo.getUsername());

        if (passwordResetToken != null) {
            passwordResetToken.setToken(token);
        } else {
            passwordResetToken = new PasswordResetToken(token, setPasswordLinkTo.getUsername());
        }
        passwordResetTokenRepository.save(passwordResetToken);

        String resetPasswordLink = resetPasswordUrl + "/" + token;

        return notificationService.sendNotificationForResetPasswordLink(setPasswordLinkTo.getEmail(), setPasswordLinkTo.getMobile(), resetPasswordLink);
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

            User user = userDaoService.findByUsername(passwordResetToken.getUserName());

            if (null == user) {
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

    /**
     * Changes password related to username
     *
     * @param resetPasswordRequestTo coming from Service
     * @return ResetPasswordResponseTo Object
     */
    @Override
    public ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) {

        User user = userRepository.findByUsername(resetPasswordRequestTo.getUsername());

        if (null != user) {
            user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequestTo.getPassword()));
            try {
                userRepository.save(user);
                return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
            } catch (Exception e) {
                return new ResponseMessageTo(NMRConstants.PROBLEM_OCCURRED);
            }
        } else {
            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

    }

    /**
     * After confirming old password, new password is created
     *
     * @param changePasswordRequestTo coming from controller
     * @return Success or failure message
     */
    @Override
    public ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) {

        User user = userRepository.findByUsername(changePasswordRequestTo.getUsername());

        if (null != user) {
            if (bCryptPasswordEncoder.matches(changePasswordRequestTo.getOldPassword(), user.getPassword())) {
                user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequestTo.getNewPassword()));
                try {
                    userRepository.save(user);
                    return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
                } catch (Exception e) {
                    return new ResponseMessageTo(NMRConstants.PROBLEM_OCCURRED);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.OLD_PASSWORD_NOT_MATCHING);
            }
        } else {
            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

    }
}

