package in.gov.abdm.nmr.db.sql.domain.user_detail;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UpdateRefreshTokenIdRequestTO;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.IUserDetailMapper;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailSearchTO;
import in.gov.abdm.nmr.db.sql.domain.user_detail.to.UserDetailTO;

@Service
@Transactional
public class UserDetailService implements IUserDetailService {

    private IUserDetailMapper userDetailMapper;

    public UserDetailService(IUserDetailMapper dtoEntityMapper) {
        this.userDetailMapper = dtoEntityMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetailTO searchUserDetail(UserDetailSearchTO userDetailSearchTO) {
        UserDetail userDetail = searchUserDetailInternal(userDetailSearchTO);
        return userDetailMapper.userDetailToDto(userDetail);
    }

    private UserDetail searchUserDetailInternal(UserDetailSearchTO userDetailSearchTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserDetail> criteria = builder.createQuery(UserDetail.class);
        Root<UserDetail> root = criteria.from(UserDetail.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(userDetailSearchTO.getUsername())) {
            predicates.add(builder.equal(root.get("username"), userDetailSearchTO.getUsername()));
        }
        criteria.select(root).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(criteria).getSingleResult();
    }

    @Override
    public String findRefreshTokenId(UserDetailSearchTO userDetailSearchTO) {
        return searchUserDetailInternal(userDetailSearchTO).getRefreshTokenId();
    }

    @Override
    public Integer updateRefreshTokenId(UpdateRefreshTokenIdRequestTO refreshTokenRequestTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<UserDetail> criteria = builder.createCriteriaUpdate(UserDetail.class);
        Root<UserDetail> root = criteria.from(UserDetail.class);

        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(refreshTokenRequestTO.getUsername())) {
            predicates.add(builder.equal(root.get("username"), refreshTokenRequestTO.getUsername()));
        }

        criteria.set(root.get("refreshTokenId"), refreshTokenRequestTO.getRefreshTokenId()).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(criteria).executeUpdate();
    }
}
