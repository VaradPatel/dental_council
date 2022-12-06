package in.gov.abdm.nmr.db.sql.domain.district;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DistrictRepository extends JpaRepository<District, BigInteger> {

    @Query(value = "SELECT id, name, code, state FROM district where state=:state", nativeQuery = true)
    List<District> getDistrict(Long state);

}
