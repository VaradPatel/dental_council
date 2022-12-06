package in.gov.abdm.nmr.db.sql.domain.hp_profile;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import in.gov.abdm.nmr.api.controller.hp.to.SmcRegistrationDetailRequestTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.HpSmcDetailTO;
import in.gov.abdm.nmr.db.sql.domain.hp_profile.to.IHpProfileMapper;
import in.gov.abdm.nmr.db.sql.domain.registration_detail.RegistrationDetail;

public class HpProfileService implements IHpProfileService {
    
    private IHpProfileMapper HpProfileMapper;
    
    public HpProfileService(IHpProfileMapper hpProfileMapper) {
        HpProfileMapper = hpProfileMapper;
    }

    @PersistenceContext
    private EntityManager entityManager;
    
    public HpSmcDetailTO fetchSmcRegistrationDetail(SmcRegistrationDetailRequestTO smcRegistrationDetailRequestTO) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<HpProfile> criteria = builder.createQuery(HpProfile.class);
        Root<HpProfile> root = criteria.from(HpProfile.class);
        Join<Object, Object> registrationDetails = root.join(HpProfile_.REGISTRATION_DETAILS, JoinType.INNER);

        List<Predicate> predicates = new ArrayList<>();
        if(StringUtils.isNotBlank(smcRegistrationDetailRequestTO.getRegistrationNumber())) {
            
        }
        
        return null;
    }
}
