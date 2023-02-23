package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.RegistrationRenewationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface RegistrationRenewationTypeRepository extends JpaRepository<RegistrationRenewationType, BigInteger> {

    @Query(value = "SELECT name, id FROM registration_renewation_type", nativeQuery = true)
    List<RegistrationRenewationType> getRegistrationRenewationType();

}
