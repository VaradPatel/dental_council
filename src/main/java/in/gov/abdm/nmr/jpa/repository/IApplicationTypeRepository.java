package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.ApplicationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface IApplicationTypeRepository extends JpaRepository<ApplicationType, BigInteger> {

}
