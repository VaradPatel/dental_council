package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.WorkNature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface WorkNatureRepository extends JpaRepository<WorkNature, BigInteger> {

 

}
