package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import in.gov.abdm.nmr.entity.ForeignQualificationDetails;

public interface IForeignQualificationDetailRepository extends JpaRepository<ForeignQualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM foreign_qualification_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<ForeignQualificationDetails> getQualificationDetailsByHpProfileId(BigInteger hpProfileId);
}
