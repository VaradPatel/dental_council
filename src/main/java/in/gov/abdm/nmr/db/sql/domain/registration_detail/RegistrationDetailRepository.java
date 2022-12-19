package in.gov.abdm.nmr.db.sql.domain.registration_detail;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationDetailRepository extends JpaRepository<RegistrationDetails, Long> {

    @Query(value = "SELECT * FROM registration_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    RegistrationDetails getRegistrationDetailsByHpProfileId(BigInteger hpProfileId);

}
