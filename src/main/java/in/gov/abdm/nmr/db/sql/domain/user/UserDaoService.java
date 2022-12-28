package in.gov.abdm.nmr.db.sql.domain.user_detail;

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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.db.sql.domain.user.to.IUserMapper;
import in.gov.abdm.nmr.db.sql.domain.user.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user.to.UserTO;

@Service
@Transactional
public class UserDaoService implements IUserDaoService {

    private IUserMapper userDetailMapper;

    private IUserepository userDetailRepository;

    private EntityManager entityManager;

    public UserDaoService(IUserMapper userDetailMapper, IUserepository userDetailRepository, EntityManager entityManager) {
        super();
        this.userDetailMapper = userDetailMapper;
        this.userDetailRepository = userDetailRepository;
        this.entityManager = entityManager;
    }

    @Override
    public UserTO searchUserDetail(UserSearchTO userDetailSearchTO) {
        User userDetail = searchUserDetailInternal(userDetailSearchTO);
        return userDetailMapper.userDetailToDto(userDetail);
    }

    @Override
    public User searchUserDetailInternal(UserSearchTO userDetailSearchTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);
        Root<User> root = criteria.from(User.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(userDetailSearchTO.getUsername())) {
            predicates.add(builder.equal(root.get("username"), userDetailSearchTO.getUsername()));
        }
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
    public UserDetail findById(BigInteger id) {
        return userDetailRepository.findById(id).get();
    }

    @Override
    public UserDetail saveUserDetail(UserDetail userDetail) {
        return userDetailRepository.saveAndFlush(userDetail);
    }
    
    @Override
    public User findUserDetailByUsername(String username) {
        return userDetailRepository.findByUsername(username);
    }
}
