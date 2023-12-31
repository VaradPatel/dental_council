package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.Password;
import in.gov.abdm.nmr.entity.ResetToken;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.ResetTokenRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import in.gov.abdm.nmr.service.*;
import in.gov.abdm.nmr.util.NMRConstants;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementations of methods for resetting and changing password
 */
@Service
@Transactional
@Slf4j
public class PasswordServiceImpl implements IPasswordService {

    public static final String CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS = "Current password should not be same as last 5 passwords";
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${council.reset-password.url}")
    private String resetPasswordUrl;

    @Autowired
    IUserDaoService userDaoService;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

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

    @Autowired
    private IOtpValidationService otpValidationService;

    /**
     * Creates new unique token for reset password transaction
     *
     * @param sendLinkOnMailTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo getResetPasswordLink(SendLinkOnMailTo sendLinkOnMailTo, String username) {
        try {
            resetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

            if (userDaoService.existsByEmailAndUserTypeId(sendLinkOnMailTo.getEmail(), sendLinkOnMailTo.getUserType())) {
                return notificationService.sendNotificationForResetPasswordLink(sendLinkOnMailTo.getEmail(), generateLink(sendLinkOnMailTo),username);
            } else {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }

        } catch (Exception e) {
            log.error("Exception occurred while setting password link.", e);
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

            resetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

            ResetToken resetToken = resetTokenRepository.findByToken(newPasswordTo.getToken());
            if (resetToken == null || resetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {
                return new ResponseMessageTo(NMRConstants.LINK_EXPIRED);
            }
            User user = userDaoService.findByUsername(resetToken.getUserName(), resetToken.getUserType());

            if (null == user) {
                return new ResponseMessageTo(NMRConstants.USER_NOT_FOUND);
            }


            String decryptedNewPassword = rsaUtil.decrypt(newPasswordTo.getPassword());
            for (Password password : passwordDaoService.findLast5(user.getId())) {
                if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                    throw new InvalidRequestException(NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getCode(), NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getMessage());
                }
            }

            String hashedPassword = bCryptPasswordEncoder.encode(decryptedNewPassword);
            user.setPassword(hashedPassword);
            userDaoService.save(user);

            Password password = new Password(null, hashedPassword, user);
            passwordDaoService.save(password);
            resetToken.setExpiryDate(new Timestamp(System.currentTimeMillis()));
            resetTokenRepository.save(resetToken);

            return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);
        } catch (Exception e) {
            log.error("Exception occurred while setting password.", e);
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
    public ResponseMessageTo resetPassword(ResetPasswordRequestTo resetPasswordRequestTo) throws GeneralSecurityException, InvalidRequestException, OtpException {
        String transactionId = resetPasswordRequestTo.getTransactionId();
        if (otpValidationService.isOtpVerified(transactionId)) {
            throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
        }
        User user = userDaoService.findByUsername(resetPasswordRequestTo.getUsername(), resetPasswordRequestTo.getUserType());

        if (null != user) {

            String decryptedNewPassword = rsaUtil.decrypt(resetPasswordRequestTo.getPassword());
            for (Password password : passwordDaoService.findLast5(user.getId())) {
                if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                    throw new InvalidRequestException(NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getCode(), NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getMessage());
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
                log.error("Exception occurred while setting password.", e);
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
    public ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo) throws InvalidRequestException, GeneralSecurityException {

        User user = userDaoService.findById(changePasswordRequestTo.getUserId());
        if (user != null) {
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
            User loggedInUser = userDaoService.findByUsername(userName, userType);
            if (loggedInUser.getId().equals(user.getId())) {
                return changePassword(changePasswordRequestTo, user);
            } else {
                throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
            }
        } else {
            throw new InvalidRequestException(NMRConstants.USER_NOT_FOUND);
        }

    }

    private ResponseMessageTo changePassword(ChangePasswordRequestTo changePasswordRequestTo, User user) throws GeneralSecurityException, InvalidRequestException {
        if (bCryptPasswordEncoder.matches(rsaUtil.decrypt(changePasswordRequestTo.getOldPassword()), user.getPassword())) {

            String decryptedNewPassword = rsaUtil.decrypt(changePasswordRequestTo.getNewPassword());
            for (Password password : passwordDaoService.findLast5(user.getId())) {
                if (bCryptPasswordEncoder.matches(decryptedNewPassword, password.getValue())) {
                    throw new InvalidRequestException(NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getCode(), NMRError.CURRENT_PASSWORD_SHOULD_NOT_BE_SAME_AS_LAST_5_PASSWORDS.getMessage());
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
                log.error("Exception occurred while setting password.", e);
                throw new InvalidRequestException();
            }
        } else {
            throw new InvalidRequestException(NMRConstants.OLD_PASSWORD_NOT_MATCHING);
        }
    }

    @Override
    public String generateLink(SendLinkOnMailTo sendLinkOnMailTo) {


        String token = RandomString.make(30);
        ResetToken resetToken = resetTokenRepository.findByUserNameAndUserType(sendLinkOnMailTo.getEmail(), sendLinkOnMailTo.getUserType());

        if (resetToken != null) {
            resetToken.setToken(token);
            resetToken.updateExpiryTime();
        } else {
            resetToken = new ResetToken(token, sendLinkOnMailTo.getEmail(), sendLinkOnMailTo.getUserType());
        }
        resetTokenRepository.save(resetToken);

        return resetPasswordUrl + "/" + token;

    }

}

