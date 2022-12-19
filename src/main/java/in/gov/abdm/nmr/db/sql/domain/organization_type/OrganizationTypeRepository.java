package in.gov.abdm.nmr.db.sql.domain.organization_type;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, BigInteger> {

}
