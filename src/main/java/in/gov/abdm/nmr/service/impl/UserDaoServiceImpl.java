package in.gov.abdm.nmr.service.impl;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_COLLEGE_ID;
import static in.gov.abdm.nmr.util.NMRConstants.INVALID_PROFILE_ID;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.dto.NbeProfileTO;
import in.gov.abdm.nmr.dto.NmcProfileTO;
import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.dto.SMCProfileTO;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.entity.NbeProfile;
import in.gov.abdm.nmr.entity.NmcProfile;
import in.gov.abdm.nmr.entity.SMCProfile;
import in.gov.abdm.nmr.entity.StateMedicalCouncil;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.User_;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;

@Service
@Transactional
public class UserDaoServiceImpl implements IUserDaoService {

    private IUserRepository userDetailRepository;
    private ISmcProfileRepository smcProfileRepository;
    private INmcProfileRepository nmcProfileRepository;
    private INbeProfileRepository nbeProfileRepository;

    private EntityManager entityManager;

    private IAccessControlService accessControlService;
    public UserDaoServiceImpl(IUserRepository userDetailRepository, EntityManager entityManager, ISmcProfileRepository smcProfileRepository, //
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
            predicates.add(builder.or(builder.equal(root.get(User_.EMAIL), refreshTokenRequestTO.getUsername()), builder.equal(root.get(User_.MOBILE_NUMBER), //
                    refreshTokenRequestTO.getUsername()), builder.equal(root.get(User_.HPR_ID), refreshTokenRequestTO.getUsername()), //
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
    public User findByUsername(String username) {
        return userDetailRepository.findByUsername(username);
    }

    @Override
    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public boolean existsByHprId(String hprId) {
        return userDetailRepository.existsByHprId(hprId);
    }

    @Override
    public boolean existsByMobileNumber(String mobileNumber) {
        return userDetailRepository.existsByMobileNumber(mobileNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDetailRepository.existsByEmail(email);
    }



    @Override
    public User toggleSmsNotification(boolean isSmsNotificationEnabled) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDetailRepository.findByUsername(userName);
        userDetail.setSmsNotificationEnabled(isSmsNotificationEnabled);
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public User toggleEmailNotification(boolean isEmailNotificationEnabled) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDetailRepository.findByUsername(userName);
        userDetail.setEmailNotificationEnabled(isEmailNotificationEnabled);
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public User toggleNotification(NotificationToggleRequestTO notificationToggleRequestTO) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User userDetail = userDetailRepository.findByUsername(userName);
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
    public SMCProfile findSmcProfile(BigInteger id) throws NmrException {
        SMCProfile smcProfileEntity = smcProfileRepository.findById(id).orElse(null);
        if (smcProfileEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(smcProfileEntity.getUser().getId());
        return smcProfileEntity;
    }

    @Override
    public NmcProfile findNmcProfile(BigInteger id) throws NmrException {
        NmcProfile nmcProfileEntity = nmcProfileRepository.findById(id).orElse(null);
        if (nmcProfileEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(nmcProfileEntity.getUser().getId());
        return nmcProfileEntity;
    }

    @Override
    public NbeProfile findNbeProfile(BigInteger id) throws NmrException {
        NbeProfile nbeProfileEntity = nbeProfileRepository.findById(id).orElse(null);
        if (nbeProfileEntity == null) {
            throw new NmrException(INVALID_COLLEGE_ID, HttpStatus.BAD_REQUEST);
        }
        accessControlService.validateUser(nbeProfileEntity.getUser().getId());
        return nbeProfileEntity;
    }

    @Override
    public SMCProfile updateSmcProfile(BigInteger id, SMCProfileTO smcProfileTO) throws NmrException {
        SMCProfile smcProfile = smcProfileRepository.findById(id).orElse(null);
        if (smcProfile == null) {
            throw new NmrException(INVALID_PROFILE_ID, HttpStatus.BAD_REQUEST);
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
        return smcProfileRepository.save(smcProfile);
    }

    @Override
    public NmcProfile updateNmcProfile(BigInteger id, NmcProfileTO nmcProfileTO) throws NmrException {
        NmcProfile nmcProfile = nmcProfileRepository.findById(id).orElse(null);
        if (nmcProfile == null) {
            throw new NmrException(INVALID_PROFILE_ID, HttpStatus.BAD_REQUEST);
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
        return nmcProfileRepository.saveAndFlush(nmcProfile);
    }

    @Override
    public NbeProfile updateNbeProfile(BigInteger id, NbeProfileTO nbeProfileTO) throws NmrException {
        NbeProfile nbeProfile = nbeProfileRepository.findById(id).orElse(null);
        if (nbeProfile == null) {
            throw new NmrException(INVALID_PROFILE_ID, HttpStatus.BAD_REQUEST);
        }
        nbeProfile.setId(id);
        nbeProfile.setEmailId(nbeProfileTO.getEmailId());
        nbeProfile.setMobileNo(nbeProfileTO.getMobileNo());
        return nbeProfileRepository.saveAndFlush(nbeProfile);
    }
}
