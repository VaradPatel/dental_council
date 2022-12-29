package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FacilityTypeRepository extends JpaRepository<FacilityType, BigInteger> {

    @Query(value = "SELECT name, id FROM facility_type", nativeQuery = true)
    List<FacilityType> getFacilityType();

}
