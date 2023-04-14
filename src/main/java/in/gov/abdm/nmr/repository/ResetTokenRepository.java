package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.stream.Stream;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    ResetToken findByToken(String token);

    Stream<ResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Transactional
    @Query("delete from ResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

    ResetToken findByUserName(String username);
}
