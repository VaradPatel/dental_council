package in.gov.abdm.nmr.db.sql.domain.work_nature;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkNatureRepository extends JpaRepository<WorkNature, BigInteger> {

 

}
