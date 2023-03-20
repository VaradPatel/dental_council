package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.util.NMRConstants.FORBIDDEN;

import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.dto.ChangePasswordRequestTo;
import in.gov.abdm.nmr.dto.CreateHpUserAccountTo;
import in.gov.abdm.nmr.dto.ResetPasswordRequestTo;
import in.gov.abdm.nmr.dto.ResponseMessageTo;
import in.gov.abdm.nmr.dto.SetNewPasswordTo;
import in.gov.abdm.nmr.entity.HpProfile;
import in.gov.abdm.nmr.entity.Password;
import in.gov.abdm.nmr.entity.PasswordResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserGroup;
import in.gov.abdm.nmr.entity.UserSubType;
import in.gov.abdm.nmr.entity.UserType;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.PasswordResetTokenRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IPasswordDaoService;
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
    
    @Autowired
    private IPasswordDaoService passwordDaoService;

    /**
     * Creates new unique token for reset password transaction
     *
     * @param setPasswordLinkTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getResetPasswordLink(CreateHpUserAccountTo setPasswordLinkTo) {
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
                    return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
                }
            } else {
                return new ResponseMessageTo(NMRConstants.USER_ALREADY_EXISTS);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }

    public ResponseMessageTo passwordReset(CreateHpUserAccountTo setPasswordLinkTo) {
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
            
            String decryptedNewPassword = rsaUtil.decrypt(newPasswordTo.getPassword());
            for (Password password : passwordDaoService.findLast5(user.getId())) {
                if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                    throw new InvalidRequestException("Current password should not be same as last 5 passwords");
                }
            }
            
            String hashedPassword = bCryptPasswordEncoder.encode(decryptedNewPassword);
            user.setPassword(hashedPassword);
            userDaoService.save(user);
            
            Password password = new Password(null, hashedPassword, user);
            passwordDaoService.save(password);

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
     * @throws GeneralSecurityException 
     * @throws InvalidRequestException 
     */
    @Override
    public ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) throws GeneralSecurityException, InvalidRequestException {

        User user = userDaoService.findByUsername(resetPasswordRequestTo.getUsername());

        if (null != user) {
            
            String decryptedNewPassword = rsaUtil.decrypt(resetPasswordRequestTo.getPassword());
            for (Password password : passwordDaoService.findLast5(user.getId())) {
                if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                    throw new InvalidRequestException("Current password should not be same as last 5 passwords");
                }
            }

            try {
                String hashedPassword = bCryptPasswordEncoder.encode(decryptedNewPassword);
                user.setPassword(hashedPassword);
                userDaoService.save(user);

                Password password = new Password(null, hashedPassword, user);
                passwordDaoService.save(password);
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
     * @throws GeneralSecurityException 
     */
    @Override
    public ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) throws InvalidRequestException, GeneralSecurityException{

        User user = userDaoService.findById(changePasswordRequestTo.getUserId());
        if (user != null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            User loggedInUser = userDaoService.findByUsername(userName);
            if (loggedInUser.getId().equals(user.getId())) {

                if (bCryptPasswordEncoder.matches(rsaUtil.decrypt(changePasswordRequestTo.getOldPassword()), user.getPassword())) {
                    
                    String decryptedNewPassword = rsaUtil.decrypt(changePasswordRequestTo.getNewPassword());
                    for (Password password : passwordDaoService.findLast5(user.getId())) {
                        if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                            throw new InvalidRequestException("Current password should not be same as last 5 passwords");
                        }
                    }
                    
                    String hashedPassword = bCryptPasswordEncoder.encode(decryptedNewPassword);
                    user.setPassword(hashedPassword);
                    try {
                        user = userDaoService.save(user);
                        Password password = new Password(null, hashedPassword, user);
                        passwordDaoService.save(password);
                        
                        return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
                    } catch (Exception e) {
                        return new ResponseMessageTo(NMRConstants.PROBLEM_OCCURRED);
                    }
                } else {
                    return new ResponseMessageTo(NMRConstants.OLD_PASSWORD_NOT_MATCHING);
                }
            } else {

                throw new AccessDeniedException(FORBIDDEN);
            }
        } else {

            return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
        }

    }
}

