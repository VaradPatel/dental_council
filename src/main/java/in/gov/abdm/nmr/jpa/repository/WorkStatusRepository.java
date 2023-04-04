package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface WorkStatusRepository extends JpaRepository<WorkStatus, BigInteger> {


}
