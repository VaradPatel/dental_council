package in.gov.abdm.nmr.repository;

import in.gov.abdm.nmr.entity.CollegeMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface CollegeMasterRepository extends JpaRepository<CollegeMaster, BigInteger> {
    @Query(value = "SELECT *  FROM college_master WHERE state_id= :stateId", nativeQuery = true)
    List<CollegeMaster> getCollegesByStateId(BigInteger stateId);
}
