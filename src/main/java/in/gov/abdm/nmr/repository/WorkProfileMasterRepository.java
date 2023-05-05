package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkProfileMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;

public interface WorkProfileMasterRepository extends JpaRepository<WorkProfileMaster, BigInteger> {

}
