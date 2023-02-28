package in.gov.abdm.nmr.service.impl;

import in.gov.abdm.nmr.dto.*;
import in.gov.abdm.nmr.entity.*;
import in.gov.abdm.nmr.exception.NmrException;
import in.gov.abdm.nmr.mapper.IUserMapper;
import in.gov.abdm.nmr.repository.INbeProfileRepository;
import in.gov.abdm.nmr.repository.INmcProfileRepository;
import in.gov.abdm.nmr.repository.ISmcProfileRepository;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IAccessControlService;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static in.gov.abdm.nmr.util.NMRConstants.INVALID_COLLEGE_ID;
import static in.gov.abdm.nmr.util.NMRConstants.INVALID_PROFILE_ID;

@Service
@Transactional
public class UserDaoServiceImpl implements IUserDaoService {

    private IUserMapper userDetailMapper;

    private IUserRepository userDetailRepository;
    private ISmcProfileRepository smcProfileRepository;
    private INmcProfileRepository nmcProfileRepository;
    private INbeProfileRepository nbeProfileRepository;

    private EntityManager entityManager;

    private IAccessControlService accessControlService;
    public UserDaoServiceImpl(IUserMapper userDetailMapper, IUserRepository userDetailRepository, EntityManager entityManager, ISmcProfileRepository smcProfileRepository, //
                              INmcProfileRepository nmcProfileRepository, INbeProfileRepository nbeProfileRepository, IAccessControlService accessControlService) {
        super();
        this.userDetailMapper = userDetailMapper;
        this.userDetailRepository = userDetailRepository;
        this.entityManager = entityManager;
        this.smcProfileRepository = smcProfileRepository;
        this.nmcProfileRepository = nmcProfileRepository;
        this.nbeProfileRepository = nbeProfileRepository;
        this.accessControlService = accessControlService;
    }

    @Override
    public UserTO searchUserDetail(UserSearchTO userDetailSearchTO) {
        User userDetail = searchUserDetailInternal(userDetailSearchTO);
        return userDetailMapper.userToDto(userDetail);
    }

    @Override
    public User searchUserDetailInternal(UserSearchTO userDetailSearchTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get("username"), userDetailSearchTO.getUsername()));
        criteria.select(root).where(predicates.toArray(new Predicate[0]));
        try {
            return entityManager.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public String findRefreshTokenId(UserSearchTO userDetailSearchTO) {
        return searchUserDetailInternal(userDetailSearchTO).getRefreshTokenId();
    }

    @Override
    public Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> criteria = builder.createCriteriaUpdate(User.class);
        Root<User> root = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(refreshTokenRequestTO.getUsername())) {
            predicates.add(builder.equal(root.get(User_.USERNAME), refreshTokenRequestTO.getUsername()));
        }

        criteria.set(root.get(User_.REFRESH_TOKEN_ID), refreshTokenRequestTO.getRefreshTokenId()).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteria).executeUpdate();
    }

    @Override
    public User findById(BigInteger id) {
        return userDetailRepository.findById(id).get();
    }

    @Override
    public User saveUserDetail(User userDetail) {
        return userDetailRepository.saveAndFlush(userDetail);
    }

    @Override
    public User findByUsername(String username) {
        return userDetailRepository.findByUsername(username);
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
        StateMedicalCouncil stateMedicalCouncil = new StateMedicalCouncil();
        stateMedicalCouncil.setId(smcProfileTO.getStateMedicalCouncil().getId());
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
