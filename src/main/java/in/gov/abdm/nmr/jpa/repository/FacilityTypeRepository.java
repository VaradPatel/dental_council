package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface FacilityTypeRepository extends JpaRepository<FacilityType, BigInteger> {

    @Query(value = "SELECT name, id FROM facility_type", nativeQuery = true)
    List<FacilityType> getFacilityType();

}
