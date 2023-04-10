package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IApplicationTypeRepository extends JpaRepository<ApplicationType, BigInteger> {

}
