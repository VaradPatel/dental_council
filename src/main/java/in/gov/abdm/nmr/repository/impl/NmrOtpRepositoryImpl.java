package in.gov.abdm.nmr.repository.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.gov.abdm.nmr.repository.NmrOtpRepository;
import in.gov.abdm.nmr.entity.Otp;
import in.gov.abdm.nmr.repository.OtpRepository;

@Repository
public class NmrOtpRepositoryImpl implements NmrOtpRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OtpRepository otpRepository;

    @Override
    public Boolean doesValidDuplicateOtpExists(String otp, String email) {
        return !otpRepository.findByExpiredIsFalseAndOtpHashIsAndContactNot(otp, email).isEmpty();
    }

    @Override
    public Otp saveOtpDetails(String otp, String email) {
        List<Otp> previousOtps = otpRepository.findByExpiredIsFalseAndContactIs(email);
        for (Otp previousOtp : previousOtps) {
            previousOtp.setExpired(true);
            otpRepository.flush();
        }

        Otp otpDetails = new Otp(UUID.randomUUID().toString(), otp, Timestamp.valueOf(LocalDateTime.now()),
                Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)), false, 0, email);
        while (otpRepository.existsById(otpDetails.getId())) {
            otpDetails.setId(UUID.randomUUID().toString());
        }
        return otpRepository.saveAndFlush(otpDetails);
    }

    @Override
    public Long findOtpGeneratedInLast15Minutes(String email) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Otp> root = criteria.from(Otp.class);
        Predicate emailEquals = builder.equal(root.get("contact"), email);
        Predicate inLast15Minutes = builder.greaterThanOrEqualTo(root.get("createdAt"),
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(15)));
        return em.createQuery(criteria.select(builder.count(root)).where(builder.and(emailEquals, inLast15Minutes)))
                .getSingleResult();
    }

    @Override
    public Long findOtpGeneratedInLast10MinutesWithExceededAttempts(String email) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
        Root<Otp> root = criteria.from(Otp.class);
        Predicate emailEquals = builder.equal(root.get("contact"), email);
        Predicate inLast10Minutes = builder.greaterThanOrEqualTo(root.get("createdAt"),
                Timestamp.valueOf(LocalDateTime.now().minusMinutes(10)));
        Predicate countGreaterThan3 = builder.greaterThanOrEqualTo(root.get("attempts"), 3);
        return em.createQuery(criteria.select(builder.count(root))
                .where(builder.and(emailEquals, inLast10Minutes, countGreaterThan3))).getSingleResult();
    }


}
