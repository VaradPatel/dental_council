package in.gov.abdm.nmr.db.sql.domain.registration_renewation_type;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRenewationTypeRepository extends JpaRepository<RegistrationRenewationType, BigInteger> {

    @Query(value = "SELECT name, id FROM registration_renewation_type", nativeQuery = true)
    List<RegistrationRenewationType> getRegistrationRenewationType();

}
