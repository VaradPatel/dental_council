package in.gov.abdm.nmr.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import in.gov.abdm.nmr.dto.NotificationToggleRequestTO;
import in.gov.abdm.nmr.entity.User;
import in.gov.abdm.nmr.entity.User_;
import in.gov.abdm.nmr.repository.IUserRepository;
import in.gov.abdm.nmr.service.IUserDaoService;
import in.gov.abdm.nmr.util.NMRConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.mapper.IUserMapper;
import in.gov.abdm.nmr.dto.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.dto.UserSearchTO;
import in.gov.abdm.nmr.dto.UserTO;

@Service
@Transactional
public class UserDaoServiceImpl implements IUserDaoService {

    private IUserMapper userDetailMapper;

    private IUserRepository userDetailRepository;

    private EntityManager entityManager;

    public UserDaoServiceImpl(IUserMapper userDetailMapper, IUserRepository userDetailRepository, EntityManager entityManager) {
        super();
        this.userDetailMapper = userDetailMapper;
        this.userDetailRepository = userDetailRepository;
        this.entityManager = entityManager;
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
    public User findUserDetailByUsername(String username) {
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
            if(NMRConstants.SMS.equalsIgnoreCase(notificationToggleTO.getMode())){
                userDetail.setSmsNotificationEnabled(notificationToggleTO.getIsEnabled());
            }else if(NMRConstants.EMAIL.equalsIgnoreCase(notificationToggleTO.getMode())){
                userDetail.setEmailNotificationEnabled(notificationToggleTO.getIsEnabled());
            }
        });
        return userDetailRepository.saveAndFlush(userDetail);
    }
}
