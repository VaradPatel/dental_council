package in.gov.abdm.nmr.db.sql.domain.college_dean;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ICollegeDeanRepository extends JpaRepository<CollegeDean, BigInteger> {

}
