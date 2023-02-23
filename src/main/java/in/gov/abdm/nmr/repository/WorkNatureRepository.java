package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.WorkNature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface WorkNatureRepository extends JpaRepository<WorkNature, BigInteger> {

 

}
