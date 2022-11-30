package in.gov.abdm.nmr.domain.sub_district;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SubDistrictRepository extends JpaRepository<SubDistrict, BigInteger> {

	@Query(value = "SELECT name, id FROM sub_district where district=:district", nativeQuery = true)
	List<Tuple> getSubDistrict(BigInteger district);

}
