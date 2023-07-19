package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Date;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    ResetToken findByToken(String token);

    @Modifying
    @Transactional
    @Query("delete from ResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);

    ResetToken findByUserNameAndUserType(String username, BigInteger userType);
}
