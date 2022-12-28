package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeDeanRepository extends JpaRepository<CollegeDean, BigInteger> {

    @Query(value = "SELECT c FROM collegeDean c join c.user usr where usr.id=:userDetailId")
    CollegeDean findByUserDetail(BigInteger userDetailId);
}
