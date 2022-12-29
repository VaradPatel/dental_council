package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, BigInteger> {


}
