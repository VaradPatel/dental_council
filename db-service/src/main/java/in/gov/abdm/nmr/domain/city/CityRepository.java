package in.gov.abdm.nmr.domain.city;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, BigInteger> {

	@Query(value = "SELECT name, id FROM city where subdistrict=:sub_district", nativeQuery = true)
	List<Tuple> getCity(BigInteger sub_district);

}
