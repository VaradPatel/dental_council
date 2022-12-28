package in.gov.abdm.nmr.db.sql.domain.work_status;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, BigInteger> {


}
