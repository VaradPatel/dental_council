package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.exception.InvalidIdException;
import in.gov.abdm.nmr.exception.InvalidRequestException;
import in.gov.abdm.nmr.exception.NMRError;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.security.username_password.UserPasswordAuthenticationToken;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserDaoServiceImpl implements IUserDaoService {

    private IUserRepository userDetailRepository;
    private ISmcProfileRepository smcProfileRepository;
    private INmcProfileRepository nmcProfileRepository;
    private INbeProfileRepository nbeProfileRepository;

    private EntityManager entityManager;

    private IAccessControlService accessControlService;
    public UserDaoServiceImpl(IUserRepository userDetailRepository, EntityManager entityManager, ISmcProfileRepository smcProfileRepository,
                              INmcProfileRepository nmcProfileRepository, INbeProfileRepository nbeProfileRepository, IAccessControlService accessControlService) {
        super();
        this.userDetailRepository = userDetailRepository;
        this.entityManager = entityManager;
        this.smcProfileRepository = smcProfileRepository;
        this.nmcProfileRepository = nmcProfileRepository;
        this.nbeProfileRepository = nbeProfileRepository;
        this.accessControlService = accessControlService;
    }

    @Override
    public Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> criteria = builder.createCriteriaUpdate(User.class);
        Root<User> root = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(refreshTokenRequestTO.getUsername())) {
            predicates.add(builder.or(builder.equal(root.get(User_.EMAIL), refreshTokenRequestTO.getUsername()), builder.equal(root.get(User_.MOBILE_NUMBER),
                    refreshTokenRequestTO.getUsername()), builder.equal(root.get(User_.HPR_ID), refreshTokenRequestTO.getUsername()),
                    builder.equal(root.get(User_.NMR_ID), refreshTokenRequestTO.getUsername())));
        }

        criteria.set(root.get(User_.REFRESH_TOKEN_ID), refreshTokenRequestTO.getRefreshTokenId()).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteria).executeUpdate();
    }

    @Override
    public User findById(BigInteger id) {
        return userDetailRepository.findById(id).orElse(new User());
    }

    @Override
    public User save(User user) {
        return userDetailRepository.saveAndFlush(user);
    }

    @Override
    public User findByUsername(String username, BigInteger userType) {
        return userDetailRepository.findByUsername(username, userType);
    }

    @Override
    public User findFirstByMobileNumber(String mobileNumber) {
        return userDetailRepository.findFirstByMobileNumber(mobileNumber);
    }

    @Override
    public boolean existsByUserNameAndUserTypeId(String userName, BigInteger userType) {
        return userDetailRepository.existsByUserNameAndUserTypeId(userName, userType);
    }

    @Override
    public boolean existsByMobileNumberAndUserTypeId(String mobileNumber, BigInteger userType) {
        return userDetailRepository.existsByMobileNumberAndUserTypeId(mobileNumber, userType);
    }

    @Override
    public boolean existsByEmailAndUserTypeId(String email, BigInteger userType) {
        return userDetailRepository.existsByEmailAndUserTypeId(email, userType);
    }



    @Override
    public User toggleSmsNotification(boolean isSmsNotificationEnabled) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType=((UserPasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType();
        User userDetail = userDetailRepository.findByUsername(userName, userType);
        userDetail.setSmsNotificationEnabled(isSmsNotificationEnabled);
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public User toggleEmailNotification(boolean isEmailNotificationEnabled) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType=((UserPasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType();
        User userDetail = userDetailRepository.findByUsername(userName, userType);
        userDetail.setEmailNotificationEnabled(isEmailNotificationEnabled);
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        BigInteger userType=((UserPasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication()).getUserType();
        User userDetail = userDetailRepository.findByUsername(userName, userType);
        notificationToggleRequestTO.getNotificationToggles().forEach(notificationToggleTO -> {
            if (NMRConstants.SMS.equalsIgnoreCase(notificationToggleTO.getMode())) {
                userDetail.setSmsNotificationEnabled(notificationToggleTO.getIsEnabled());
            } else if (NMRConstants.EMAIL.equalsIgnoreCase(notificationToggleTO.getMode())) {
                userDetail.setEmailNotificationEnabled(notificationToggleTO.getIsEnabled());
            }
        });
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public SMCProfile findSmcProfile(BigInteger id) throws InvalidIdException {
        SMCProfile smcProfileEntity = smcProfileRepository.findById(id).orElse(null);
        if (smcProfileEntity == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        accessControlService.validateUser(smcProfileEntity.getUser().getId());
        return smcProfileEntity;
    }

    @Override
    public NmcProfile findNmcProfile(BigInteger id) throws InvalidIdException {
        NmcProfile nmcProfileEntity = nmcProfileRepository.findById(id).orElse(null);
        if (nmcProfileEntity == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        accessControlService.validateUser(nmcProfileEntity.getUser().getId());
        return nmcProfileEntity;
    }

    @Override
    public NbeProfile findNbeProfile(BigInteger id) throws InvalidIdException {
        NbeProfile nbeProfileEntity = nbeProfileRepository.findById(id).orElse(null);
        if (nbeProfileEntity == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(),NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        accessControlService.validateUser(nbeProfileEntity.getUser().getId());
        return nbeProfileEntity;
    }

    @Override
    public SMCProfile updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws InvalidIdException, InvalidRequestException {
        SMCProfile smcProfile = smcProfileRepository.findById(id).orElse(null);
        if (smcProfile == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(), NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        smcProfile.setId(id);
        smcProfile.setFirstName(smcProfileTO.getFirstName());
        smcProfile.setMiddleName(smcProfileTO.getMiddleName());
        smcProfile.setLastName(smcProfileTO.getLastName());
        smcProfile.setDisplayName(smcProfileTO.getDisplayName());
        smcProfile.setEnrolledNumber(smcProfileTO.getEnrolledNumber());
        smcProfile.setNdhmEnrollment(smcProfileTO.getNdhmEnrollment());
        smcProfile.setMobileNo(smcProfileTO.getMobileNo());
        smcProfile.setEmailId(smcProfileTO.getEmailId());
        StateMedicalCouncil stateMedicalCouncil = smcProfile.getStateMedicalCouncil();
        stateMedicalCouncil.setId(smcProfileTO.getStateMedicalCouncil().getId());
        stateMedicalCouncil.setName(smcProfileTO.getStateMedicalCouncil().getName());
        smcProfile.setStateMedicalCouncil(stateMedicalCouncil);
        User user = smcProfile.getUser();

        if (user != null) {
            if (userDetailRepository.checkEmailUsedByOtherUser(user.getId(), smcProfile.getEmailId(),user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.EMAIL_USED_BY_OTHER_USER);
            }

            if (userDetailRepository.checkMobileUsedByOtherUser(user.getId(), smcProfileTO.getMobileNo(), user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.MOBILE_USED_BY_OTHER_USER);
            }
        } else {
            throw new InvalidRequestException(NMRConstants.USER_NOT_FOUND);
        }
        user.setEmail(smcProfile.getEmailId());
        user.setMobileNumber(smcProfile.getMobileNo());
        userDetailRepository.save(user);
        return smcProfileRepository.save(smcProfile);
    }

    @Override
    public NmcProfile updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws InvalidIdException, InvalidRequestException {
        NmcProfile nmcProfile = nmcProfileRepository.findById(id).orElse(null);
        if (nmcProfile == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(),NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        nmcProfile.setId(id);
        nmcProfile.setFirstName(nmcProfileTO.getFirstName());
        nmcProfile.setMiddleName(nmcProfileTO.getMiddleName());
        nmcProfile.setLastName(nmcProfileTO.getLastName());
        nmcProfile.setDisplayName(nmcProfileTO.getDisplayName());
        nmcProfile.setEnrolledNumber(nmcProfileTO.getEnrolledNumber());
        nmcProfile.setNdhmEnrollment(nmcProfileTO.getNdhmEnrollment());
        nmcProfile.setEmailId(nmcProfileTO.getEmailId());
        nmcProfile.setMobileNo(nmcProfileTO.getMobileNo());
        User user = nmcProfile.getUser();

        if (user != null) {
            if (userDetailRepository.checkEmailUsedByOtherUser(user.getId(), nmcProfile.getEmailId(), user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.EMAIL_USED_BY_OTHER_USER);
            }

            if (userDetailRepository.checkMobileUsedByOtherUser(user.getId(), nmcProfile.getMobileNo(), user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.MOBILE_USED_BY_OTHER_USER);
            }
        } else {
            throw new InvalidRequestException(NMRConstants.USER_NOT_FOUND);
        }
        user.setEmail(nmcProfile.getEmailId());
        user.setMobileNumber(nmcProfile.getMobileNo());
        userDetailRepository.save(user);
        return nmcProfileRepository.saveAndFlush(nmcProfile);
    }

    @Override
    public NbeProfile updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws InvalidIdException, InvalidRequestException {
        NbeProfile nbeProfile = nbeProfileRepository.findById(id).orElse(null);
        if (nbeProfile == null) {
            throw new InvalidIdException(NMRError.INVALID_ID_EXCEPTION.getCode(),NMRError.INVALID_ID_EXCEPTION.getMessage());
        }
        nbeProfile.setId(id);
        nbeProfile.setDisplayName(nbeProfileTO.getDisplayName());
        nbeProfile.setEmailId(nbeProfileTO.getEmailId());
        nbeProfile.setMobileNo(nbeProfileTO.getMobileNo());
        User user = nbeProfile.getUser();

        if (user != null) {
            if (userDetailRepository.checkEmailUsedByOtherUser(user.getId(), nbeProfile.getEmailId(), user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.EMAIL_USED_BY_OTHER_USER);
            }

            if (userDetailRepository.checkMobileUsedByOtherUser(user.getId(), nbeProfile.getMobileNo(), user.getUserType().getId())) {
                throw new InvalidRequestException(NMRConstants.MOBILE_USED_BY_OTHER_USER);
            }
        } else {
            throw new InvalidRequestException(NMRConstants.USER_NOT_FOUND);
        }
        user.setEmail(nbeProfile.getEmailId());
        user.setMobileNumber(nbeProfile.getMobileNo());
        userDetailRepository.save(user);

        return nbeProfileRepository.saveAndFlush(nbeProfile);
    }

    public boolean checkEmailUsedByOtherUser(BigInteger id, String email, BigInteger userType){
        return userDetailRepository.checkEmailUsedByOtherUser(id,email,userType);
    }

    @Override
    public void unlockUser(BigInteger userId) {
        userDetailRepository.unlockUser(userId);
    }

    @Override
    public void updateLastLogin(BigInteger userId) {
        userDetailRepository.updateLastLogin(userId);
    }

    @Override
    public List<String> getUserNames(String mobileNumber, BigInteger userType) {
        return userDetailRepository.getUserNamesByMobileNumAnduserType(mobileNumber, userType);
    }
}
