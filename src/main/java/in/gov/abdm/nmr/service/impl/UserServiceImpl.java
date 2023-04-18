package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.enums.Group;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.exception.OtpException;
import in.gov.abdm.nmr.repository.IRegistrationDetailRepository;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.repository.IHpProfileRepository;
import in.gov.abdm.nmr.repository.ResetTokenRepository;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IPasswordDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private ISmcMapper smcMapper;
    @Autowired
    private INmcMapper nmcMapper;
    @Autowired
    private INbeMapper nbeMapper;

    private IUserDaoService userDaoService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    RsaUtil rsaUtil;

    @Autowired
    IHpProfileRepository hpProfileRepository;

    @Autowired
    private IPasswordDaoService passwordDaoService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    INotificationService notificationService;

    @Autowired
    OtpServiceImpl otpService;

    @Autowired
    IRegistrationDetailRepository iRegistrationDetailRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    public UserServiceImpl(IUserDaoService userDaoService) {
        this.userDaoService = userDaoService;
    }

    @Override
    public NotificationToggleResponseTO toggleSmsNotification(boolean isSmsNotificationEnabled) {
        User userDetail = userDaoService.toggleSmsNotification(isSmsNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), NMRConstants.SMS, userDetail.isSmsNotificationEnabled());
    }

    @Override
    public NotificationToggleResponseTO toggleEmailNotification(boolean isEmailNotificationEnabled) {
        User userDetail = userDaoService.toggleEmailNotification(isEmailNotificationEnabled);
        return new NotificationToggleResponseTO(userDetail.getId(), NMRConstants.EMAIL, userDetail.isEmailNotificationEnabled());
    }

    @Override
    public List<NotificationToggleResponseTO> toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO) {
        User userDetail = userDaoService.toggleNotification(notificationToggleRequestTO);
        return List.of(NotificationToggleResponseTO.builder().userId(userDetail.getId()).mode(NMRConstants.SMS).enabled(userDetail.isSmsNotificationEnabled()).build(),
                NotificationToggleResponseTO.builder().userId(userDetail.getId()).mode(NMRConstants.EMAIL).enabled(userDetail.isEmailNotificationEnabled()).build());
    }

    @Override
    public SMCProfileTO getSmcProfile(BigInteger id) throws NmrException, InvalidIdException {
        return smcMapper.smcProfileToDto(userDaoService.findSmcProfile(id));
    }

    @Override
    public NmcProfileTO getNmcProfile(BigInteger id) throws NmrException, InvalidIdException {
        return nmcMapper.nmcProfileToDto(userDaoService.findNmcProfile(id));
    }

    @Override
    public NbeProfileTO getNbeProfile(BigInteger id) throws NmrException, InvalidIdException {
        return nbeMapper.nbeProfileToDto(userDaoService.findNbeProfile(id));
    }

    @Override
    public SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException {
        return smcMapper.smcProfileToDto(userDaoService.updateSmcProfile(id, smcProfileTO));
    }

    @Override
    public NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException {
        return nmcMapper.nmcProfileToDto(userDaoService.updateNmcProfile(id, nmcProfileTO));
    }

    @Override
    public NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException {
        return nbeMapper.nbeProfileToDto(userDaoService.updateNbeProfile(id, nbeProfileTO));
    }

    @Override
    public ResponseMessageTo createHpUserAccount(CreateHpUserAccountTo createHpUserAccountTo) {


        if (userDaoService.existsByUserName(createHpUserAccountTo.getUsername())) {
            return new ResponseMessageTo(NMRConstants.USERNAME_ALREADY_EXISTS);
        }

        if (userDaoService.existsByMobileNumber(createHpUserAccountTo.getMobile())) {
            return new ResponseMessageTo(NMRConstants.MOBILE_NUMBER_ALREADY_EXISTS);
        }

        if (createHpUserAccountTo.getEmail() != null && userDaoService.existsByEmail(createHpUserAccountTo.getEmail())) {
            return new ResponseMessageTo(NMRConstants.EMAIL_ALREADY_EXISTS);
        }

        try {

            RegistrationDetails registrationDetails =
                    iRegistrationDetailRepository.fetchHpProfileIdByRegistrationNumberAndStateMedicalCouncilId(createHpUserAccountTo.getRegistrationNumber(), createHpUserAccountTo.getStateMedicalCouncilId());
            HpProfile hpProfile = registrationDetails.getHpProfileId();

            String hashedPassword = bCryptPasswordEncoder.encode(rsaUtil.decrypt(createHpUserAccountTo.getPassword()));
            User userDetail = new User(null, createHpUserAccountTo.getEmail(), createHpUserAccountTo.getMobile(), null, hashedPassword, null, true, true, //

                    entityManager.getReference(UserType.class, UserTypeEnum.HEALTH_PROFESSIONAL.getCode()), entityManager.getReference(UserSubType.class, UserSubTypeEnum.COLLEGE.getCode()), entityManager.getReference(UserGroup.class, Group.HEALTH_PROFESSIONAL.getId()), true, 0, null, createHpUserAccountTo.getUsername(), createHpUserAccountTo.getHprId(), createHpUserAccountTo.getHprIdNumber(),createHpUserAccountTo.isNew(),false);
            userDaoService.save(userDetail);
            Password password = new Password(null, hashedPassword, userDetail);
            passwordDaoService.save(password);

            hpProfile.setUser(userDetail);
            if (StringUtils.isNotBlank(createHpUserAccountTo.getMobile())) {
                hpProfile.setMobileNumber(createHpUserAccountTo.getMobile());
            }
            hpProfileRepository.save(hpProfile);
            notificationService.sendNotificationForAccountCreation(createHpUserAccountTo.getUsername(), createHpUserAccountTo.getMobile());
            return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);

        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }

    @Override
    public String retrieveUser(RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException {
        String transactionId = retrieveUserRequestTo.getTransactionId();
        if (otpService.isOtpVerified(transactionId)) {
            throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage(),
                    HttpStatus.UNAUTHORIZED.toString());
        }
        User user = userDaoService.findFirstByMobileNumber(retrieveUserRequestTo.getContact());
        if (user.getUserName() != null) {
            return user.getUserName();
        }
        return user.getEmail();
    }

    /**
     * Creates new unique token for reset password transaction
     *
     * @param verifyEmailTo email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public ResponseMessageTo verifyEmail(VerifyEmailTo verifyEmailTo) {
        try {

            resetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

            ResetToken resetToken = resetTokenRepository.findByToken(verifyEmailTo.getToken());

            if (resetToken != null) {

                if (resetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {

                    return new ResponseMessageTo(NMRConstants.LINK_EXPIRED);
                }

                User user = userDaoService.findByUsername(resetToken.getUserName());
                user.setEmailVerified(true);
                userDaoService.save(user);
                return new ResponseMessageTo(NMRConstants.SUCCESS_RESPONSE);

            } else {
                return new ResponseMessageTo(NMRConstants.LINK_EXPIRED);
            }
        } catch (Exception e) {
            return new ResponseMessageTo(e.getLocalizedMessage());
        }
    }
}
