package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ICollegeDeanRepository extends JpaRepository<CollegeDean, BigInteger> {

    @Query(value = "SELECT cd.* FROM college_dean cg inner join user ud on cg.user_id = ud.id where ud.id=:userDetailId", nativeQuery = true)
    CollegeDean findByUserDetail(BigInteger userDetailId);
}
