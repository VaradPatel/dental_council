package in.gov.abdm.nmr.repository;

import java.math.BigInteger;

import in.gov.abdm.nmr.entity.WorkNature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkNatureRepository extends JpaRepository<WorkNature, BigInteger> {

 

}
