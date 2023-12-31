package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.ForeignQualificationDetailsMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface IForeignQualificationDetailMasterRepository extends JpaRepository<ForeignQualificationDetailsMaster, BigInteger> {

    @Query(value = "SELECT * FROM foreign_qualification_details_master where hp_profile_id = :hpProfileId and is_verified=1", nativeQuery = true)
    List<ForeignQualificationDetailsMaster> getQualificationDetailsByHpProfileId(BigInteger hpProfileId);
}
