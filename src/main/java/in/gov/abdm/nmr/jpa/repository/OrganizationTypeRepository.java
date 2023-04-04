package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.OrganizationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, BigInteger> {

}
