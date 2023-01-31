package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ForeignQualificationDetails;
import in.gov.abdm.nmr.entity.QualificationDetails;
import in.gov.abdm.nmr.entity.QualificationDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public interface IQualificationDetailMasterRepository extends JpaRepository<QualificationDetailsMaster, BigInteger> {

    @Query(value = "SELECT * FROM qualification_details_master where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<QualificationDetailsMaster> getQualificationDetailsByHpProfileId(BigInteger hpProfileId);

}
