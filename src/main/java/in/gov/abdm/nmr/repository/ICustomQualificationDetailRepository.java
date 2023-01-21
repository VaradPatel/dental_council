package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import in.gov.abdm.nmr.entity.CustomQualificationDetails;
import in.gov.abdm.nmr.entity.QualificationDetails;

public interface ICustomQualificationDetailRepository extends JpaRepository<CustomQualificationDetails, BigInteger> {

    @Query(value = "SELECT * FROM custom_qualification_details where hp_profile_id = :hpProfileId", nativeQuery = true)
    List<CustomQualificationDetails> getQualificationDetailsByHpProfileId(BigInteger hpProfileId);
}
