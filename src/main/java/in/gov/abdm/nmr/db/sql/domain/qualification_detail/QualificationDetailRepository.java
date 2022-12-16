package in.gov.abdm.nmr.db.sql.domain.qualification_detail;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QualificationDetailRepository extends JpaRepository<QualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details where registration_details_id = :registrationId", nativeQuery = true)
    List<QualificationDetails> getQualificationDetailsByRegistrationId(Integer registrationId);

}
