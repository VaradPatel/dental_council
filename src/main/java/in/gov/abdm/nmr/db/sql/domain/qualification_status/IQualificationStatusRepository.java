package in.gov.abdm.nmr.db.sql.domain.qualification_status;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IQualificationStatusRepository extends JpaRepository<QualificationStatus, BigInteger> {

}
