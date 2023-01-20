package in.gov.abdm.nmr.repository;

import java.math.BigInteger;
import java.util.List;

import in.gov.abdm.nmr.entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DistrictRepository extends JpaRepository<District, BigInteger> {

    @Query(value = "SELECT id, name, iso_code, state_id, created_at, updated_at FROM district where state_id=:state", nativeQuery = true)
    List<District> getDistrict(BigInteger state);

    District findByName(String name);
}
