package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.RegistrationRenewationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRenewationTypeRepository extends JpaRepository<RegistrationRenewationType, BigInteger> {

    @Query(value = "SELECT name, id FROM registration_renewation_type", nativeQuery = true)
    List<RegistrationRenewationType> getRegistrationRenewationType();

}
