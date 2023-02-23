package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, BigInteger> {


}
