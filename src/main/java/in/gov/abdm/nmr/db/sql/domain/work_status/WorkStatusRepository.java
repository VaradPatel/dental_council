package in.gov.abdm.nmr.db.sql.domain.work_status;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, BigInteger> {


}
