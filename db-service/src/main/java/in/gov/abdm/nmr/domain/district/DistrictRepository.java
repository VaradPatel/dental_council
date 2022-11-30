package in.gov.abdm.nmr.domain.district;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DistrictRepository extends JpaRepository<District, BigInteger> {

	@Query(value = "SELECT name, id FROM district where state=:state", nativeQuery = true)
	List<Tuple> getDistrict(BigInteger state);

}
