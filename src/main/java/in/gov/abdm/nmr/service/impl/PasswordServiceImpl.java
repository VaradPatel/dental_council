package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.util.NMRConstants.FORBIDDEN;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.GetSetPasswordLinkTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.SneakyThrows;
import net.bytebuddy.utility.RandomString;

/**
 * Implementations of methods for resetting and changing password
 */
@Service
@Transactional
public class PasswordServiceImpl implements IPasswordService {

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

    @Autowired
    RsaUtil rsaUtil;

    @Autowired
    IHpProfileRepository hpProfileRepository;

    /**
     * Creates new unique token for reset password transaction
     *
     * @param setPasswordLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getResetPasswordLink(GetSetPasswordLinkTo setPasswordLinkTo) {
        try {
            if (!userDaoService.existsByUsername(setPasswordLinkTo.getUsername())) {
                User userDetail = new User(null, setPasswordLinkTo.getEmail(), setPasswordLinkTo.getMobile(), setPasswordLinkTo.getUsername(), null, null, null, true, true, //
                        entityManager.getReference(UserType.class, UserTypeEnum.HEALTH_PROFESSIONAL.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserGroup.class, Group.HEALTH_PROFESSIONAL.getId()), true, 0, null);
                userDaoService.save(userDetail);

                List<HpProfile> hpProfileList=hpProfileRepository.findHpProfileByRegistrationId(setPasswordLinkTo.getRegistrationNumber());
                List<HpProfile> hpProfiles= new ArrayList<>();
                hpProfileList.forEach(hpProfile -> {
                    hpProfile.setUser(userDetail);
                    hpProfile.setMobileNumber(setPasswordLinkTo.getMobile());
                    hpProfiles.add(hpProfile);
                });

                hpProfileRepository.saveAll(hpProfiles);

                passwordResetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

                if (userDaoService.existsByUsername(setPasswordLinkTo.getUsername())) {
                    return passwordReset(setPasswordLinkTo);
                } else {
                    return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND, null);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.USER_ALREADY_EXISTS, null);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage(), null);
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
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND, null);
            }
            if (passwordResetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {

                return new ResponseMessageTo(NMRConstants.LINK_EXPIRED, null);
            }
            user.setPassword(bCryptPasswordEncoder.encode(rsaUtil.decrypt(newPasswordTo.getPassword())));
            userDaoService.save(user);

            return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE, null);
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage(), null);
        }
    }

    /**
     * Changes password related to username
     *
     * @param resetPasswordRequestTo coming from Service
     * @return ResetPasswordResponseTo Object
     */
    @SneakyThrows
    @Override
    public ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) {

        User user = userDaoService.findByUsername(resetPasswordRequestTo.getUsername());

        if (null != user) {
            user.setPassword(bCryptPasswordEncoder.encode(rsaUtil.decrypt(resetPasswordRequestTo.getPassword())));
            try {
                userDaoService.save(user);
                return new ResponseMessageTo(null,NMRConstants.SUCCESS_RESPONSE );
            } catch (Exception e) {
                return new ResponseMessageTo(null,NMRConstants.PROBLEM_OCCURRED);
            }
        } else {
            return new ResponseMessageTo(null,NMRConstants.USER_NOT_FOUND);
        }

    }

    /**
     * After confirming old password, new password is created
     *
     * @param changePasswordRequestTo coming from controller
     * @return Success or failure message
     */
    @SneakyThrows
    @Override
    public ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) {

        User user = userDaoService.findById(changePasswordRequestTo.getUserId());
        if (user != null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User loggedInUser = userDaoService.findByUsername(userName);
            if (loggedInUser.getId().equals(user.getId())) {

                if (bCryptPasswordEncoder.matches(rsaUtil.decrypt(changePasswordRequestTo.getOldPassword()), user.getPassword())) {
                    user.setPassword(bCryptPasswordEncoder.encode(rsaUtil.decrypt(changePasswordRequestTo.getNewPassword())));
                    try {
                        userDaoService.save(user);
                        return new ResponseMessageTo(null, NMRConstants.SUCCESS_RESPONSE);
                    } catch (Exception e) {
                        return new ResponseMessageTo(null, NMRConstants.PROBLEM_OCCURRED);
                    }
                } else {
                    return new ResponseMessageTo(null, NMRConstants.OLD_PASSWORD_NOT_MATCHING);
                }
            } else {

                throw new AccessDeniedException(FORBIDDEN);
            }
        } else {

            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND, null);
        }

    }
}

