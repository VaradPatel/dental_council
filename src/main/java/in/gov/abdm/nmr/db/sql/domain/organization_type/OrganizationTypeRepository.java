package in.gov.abdm.nmr.db.sql.domain.organization_type;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrganizationTypeRepository extends JpaRepository<OrganizationType, BigInteger> {

}
