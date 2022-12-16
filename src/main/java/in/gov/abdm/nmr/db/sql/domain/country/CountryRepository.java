package in.gov.abdm.nmr.db.sql.domain.country;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CountryRepository extends JpaRepository<Country, BigInteger> {

    @Query(value = "SELECT name, id, nationality FROM country", nativeQuery = true)
    List<Country> getCountry();
    

}
