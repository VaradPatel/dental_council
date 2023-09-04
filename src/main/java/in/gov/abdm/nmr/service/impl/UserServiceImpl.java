package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.exception.*;
import in.gov.abdm.nmr.repository.*;
import in.gov.abdm.nmr.security.jwt.JwtAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.ResetToken;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.UserSubType;
import in.gov.abdm.nmr.entity.UserType;
import in.gov.abdm.nmr.enums.UserSubTypeEnum;
import in.gov.abdm.nmr.enums.UserTypeEnum;
import in.gov.abdm.nmr.mapper.INbeMapper;
import in.gov.abdm.nmr.mapper.INmcMapper;
import in.gov.abdm.nmr.mapper.ISmcMapper;
import in.gov.abdm.nmr.security.common.RsaUtil;
import in.gov.abdm.nmr.service.INbeDaoService;
import in.gov.abdm.nmr.service.INmcDaoService;
import in.gov.abdm.nmr.service.INotificationService;
import in.gov.abdm.nmr.service.IPasswordService;
import in.gov.abdm.nmr.service.ISmcProfileDaoService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.service.IUserService;
import in.gov.abdm.nmr.util.NMRConstants;

import static in.gov.abdm.nmr.util.NMRConstants.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private ISmcMapper smcMapper;
    @Autowired
    private INmcMapper nmcMapper;
    @Autowired
    private INbeMapper nbeMapper;

    @Autowired
    private IUserDaoService userDaoService;

    @Autowired
    RsaUtil rsaUtil;

    @Autowired
    IHpProfileRepository hpProfileRepository;

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
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private INmcDaoService nmcDaoService;
    
    @Autowired
    private ISmcProfileDaoService smcProfileDaoService;
    
    @Autowired
    private INbeDaoService nbeDaoService;
    
    @Autowired
    private IPasswordService passwordService;

    @Autowired
    private IUserRepository userRepository;
    @Autowired
    IFetchUserDetailsCustomRepository fetchUserDetailsCustomRepository;


    
    private static final Map<BigInteger, List<BigInteger>> ALLOWED_USER_TYPES = Map.of(UserTypeEnum.NMC.getId(), List.of(UserSubTypeEnum.NMC_ADMIN.getId(), UserSubTypeEnum.NMC_VERIFIER.getId()),
            UserTypeEnum.SMC.getId(), List.of(UserSubTypeEnum.SMC_ADMIN.getId(), UserSubTypeEnum.SMC_VERIFIER.getId()),
            UserTypeEnum.NBE.getId(), List.of(UserSubTypeEnum.NBE_ADMIN.getId(), UserSubTypeEnum.NBE_VERIFIER.getId()));

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
    public SMCProfileTO updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException, OtpException {
        return smcMapper.smcProfileToDto(userDaoService.updateSmcProfile(id, smcProfileTO));
    }

    @Override
    public NmcProfileTO updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException, InvalidIdException, InvalidRequestException, OtpException {
        return nmcMapper.nmcProfileToDto(userDaoService.updateNmcProfile(id, nmcProfileTO));
    }

    @Override
    public NbeProfileTO updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException, InvalidIdException, InvalidRequestException, OtpException {
        return nbeMapper.nbeProfileToDto(userDaoService.updateNbeProfile(id, nbeProfileTO));
    }

    @Override
    public String retrieveUser(RetrieveUserRequestTo retrieveUserRequestTo) throws OtpException {
        String transactionId = retrieveUserRequestTo.getTransactionId();
        if (otpService.isOtpVerified(transactionId)) {
            throw new OtpException(NMRError.OTP_INVALID.getCode(), NMRError.OTP_INVALID.getMessage());
        }
        User user = userDaoService.findByMobileNumberAndUserTypeId(retrieveUserRequestTo.getContact(), retrieveUserRequestTo.getUserType());
        if (user.getUserName() != null) {
            return user.getUserName();
        }
        return user.getEmail();
    }

    /**
     * Creates new unique token for reset password transaction
     *
     * @param token email/mobile to send link
     * @return ResponseMessageTo with message
     */
    @Override
    public String verifyEmail(String token) {
           resetTokenRepository.deleteAllExpiredSince(Timestamp.valueOf(LocalDateTime.now()));

            ResetToken resetToken = resetTokenRepository.findByToken(token);

            if (resetToken != null) {

                if (resetToken.getExpiryDate().compareTo(Timestamp.valueOf(LocalDateTime.now())) < 0) {
                    return EMAIL_FAILURE_PAGE;
                }

                User user = userDaoService.findByUsername(resetToken.getUserName(), resetToken.getUserType());
                user.setEmailVerified(true);
                user.setEmailNotificationEnabled(true);
                userDaoService.save(user);
                resetToken.setExpiryDate(new Timestamp(System.currentTimeMillis()));
                resetTokenRepository.save(resetToken);
                return EMAIL_SUCCESS_PAGE;

            } else {
                return EMAIL_FAILURE_PAGE;
            }
        }


    @Override
    public UserProfileTO createUser(UserProfileTO userProfileTO) throws NmrException {
        if (!ALLOWED_USER_TYPES.keySet().contains(userProfileTO.getTypeId()) ||
                !ALLOWED_USER_TYPES.get(userProfileTO.getTypeId()).contains(userProfileTO.getSubTypeId())) {
            NMRError invalidUserType = NMRError.INVALID_USER_TYPE;
            throw new NmrException(invalidUserType.getCode(), invalidUserType.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }

        validateContactDetails(userProfileTO.getEmailId(), userProfileTO.getMobileNumber(), userProfileTO.getTypeId());

        UserType userType = entityManager.getReference(UserType.class, userProfileTO.getTypeId());
        User user = User.builder().email(userProfileTO.getEmailId()).mobileNumber(userProfileTO.getMobileNumber()).isSmsNotificationEnabled(false).isEmailNotificationEnabled(false)
                .userType(userType).userSubType(entityManager.getReference(UserSubType.class, userProfileTO.getSubTypeId())).group(userType.getGroup()).accountNonLocked(true)
                .failedAttempt(0).isNew(false).isEmailVerified(false).build();
        user = userDaoService.save(user);

        if (UserTypeEnum.NMC.getId().equals(userProfileTO.getTypeId())) {
            NmcProfile nmcProfile = new NmcProfile(null, user, null, null, null, null, 0, 0, userProfileTO.getName(), userProfileTO.getEmailId(), userProfileTO.getMobileNumber());
            nmcDaoService.save(nmcProfile);

        } else if (UserTypeEnum.SMC.getId().equals(userProfileTO.getTypeId())) {
            if (userProfileTO.getSmcId() == null) {
                NMRError stateMedicalIdNull = NMRError.STATE_MEDICAL_ID_NULL;
                throw new NmrException(stateMedicalIdNull.getCode(), stateMedicalIdNull.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
            }

            SMCProfile smcProfile = new SMCProfile(null, user, null, null, null, entityManager.getReference(StateMedicalCouncil.class, userProfileTO.getSmcId()), 0, 0, userProfileTO.getName(), userProfileTO.getEmailId(), userProfileTO.getMobileNumber());
            smcProfileDaoService.save(smcProfile);

        } else if (UserTypeEnum.NBE.getId().equals(userProfileTO.getTypeId())) {
            NbeProfile nbeProfile = new NbeProfile(null, user, null, null, null, userProfileTO.getName(), userProfileTO.getEmailId(), userProfileTO.getMobileNumber());
            nbeDaoService.save(nbeProfile);
        }

        passwordService.getResetPasswordLink(new SendLinkOnMailTo(user.getEmail(), user.getUserType().getId()), user.getUserName());
        return userProfileTO;
    }

    @Override
    public UserResponseTO getAllUser(String search, String value, int pageNo, int offset, String sortBy, String sortOrder) throws InvalidRequestException, AccessDeniedException {
        UserRequestParamsTO userRequestParamsTO = new UserRequestParamsTO();
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType= ((JwtAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType().getId();
        User userDetail = userRepository.findByUsername(userName,userType);

        if (userDetail == null) {
            log.error("User don't have permission to access get users");
            throw new AccessDeniedException(NMRError.ACCESS_DENIED_EXCEPTION.getMessage());
        }
        applyFilters(search, value, userRequestParamsTO);
        userRequestParamsTO.setUserSubTypeID(userDetail.getUserSubType() != null ? userDetail.getUserSubType().getId().toString() : null);
        final String sortingOrder = (sortOrder == null || sortOrder.trim().isEmpty()) ? DEFAULT_SORT_ORDER : sortOrder;
        userRequestParamsTO.setSortOrder(sortingOrder);
        final int dataLimit = Math.min(MAX_DATA_SIZE, offset);
        Pageable pageable = PageRequest.of(pageNo, dataLimit);
        return fetchUserDetailsCustomRepository.fetchUserData(userRequestParamsTO, pageable);
    }

    private void applyFilters(String search, String value, UserRequestParamsTO userRequestParamsTO) throws InvalidRequestException {
        if (StringUtils.isNotBlank(search)) {
            if (value != null && !value.isBlank()) {
                switch (search.toLowerCase()) {
                    case USER_TYPE_ID_IN_LOWER_CASE:
                        userRequestParamsTO.setUserTypeId(value);
                        break;
                    case FIRST_NAME_IN_LOWER_CASE:
                        userRequestParamsTO.setFirstName(value);
                        break;
                    case LAST_NAME_IN_LOWER_CASE:
                        userRequestParamsTO.setLastName(value);
                        break;
                    case EMAIL_ID_IN_LOWER_CASE:
                        userRequestParamsTO.setEmailId(value);
                        break;
                    case MOBILE_NUMBER_IN_LOWER_CASE:
                        userRequestParamsTO.setMobileNumber(value);
                        break;
                    case NAME_IN_LOWER_CASE:
                        userRequestParamsTO.setName(value);
                        break;
                    default:
                        log.error("unable to complete fetch user details process due Invalid Search Criteria ");
                        throw new InvalidRequestException(NMRError.INVALID_SEARCH_CRITERIA.getCode(), NMRError.INVALID_SEARCH_CRITERIA.getMessage());
                }
            } else {
                log.error("unable to complete fetch user details process due missing search value.");
                throw new InvalidRequestException(NMRError.MISSING_SEARCH_VALUE.getCode(), NMRError.MISSING_SEARCH_VALUE.getMessage());
            }
        }
    }

    @Override
    public void deactivateUser(BigInteger userId) {
        log.info("deactivate request for user id: {}", userId);
        userRepository.deactivateUser(userId);
    }

    @Override
    public void unlockUser(BigInteger userId) {
        userDaoService.unlockUser(userId);
    }

    @Override
    public List<String> getUserNames(String mobileNumber, BigInteger userType) {
        return userDaoService.getUserNames(mobileNumber, userType);
    }

    private void validateContactDetails(String emailId, String mobileNumber, BigInteger userType) throws NmrException {
        duplicateEmailCheck(emailId, userType);
        duplicateMobileNumberCheck(mobileNumber, userType);
    }

    private void duplicateMobileNumberCheck(String mobileNumber, BigInteger userType) throws NmrException {
        if (userDaoService.existsByMobileNumberAndUserTypeId(mobileNumber, userType)) {
            NMRError mobileNumAlreadyRegistered = NMRError.MOBILE_NUM_ALREADY_REGISTERED;
            throw new NmrException(mobileNumAlreadyRegistered.getCode(), mobileNumAlreadyRegistered.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }

    private void duplicateEmailCheck(String emailId, BigInteger userType) throws NmrException {
        if (userDaoService.existsByEmailAndUserTypeId(emailId, userType)) {
            NMRError emailNumAlreadyRegistered = NMRError.EMAIL_ID_ALREADY_REGISTERED;
            throw new NmrException(emailNumAlreadyRegistered.getCode(), emailNumAlreadyRegistered.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
    }
}
