package in.gov.abdm.nmr.jpa.repository;

import in.gov.abdm.nmr.jpa.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.List;

public interface CountryRepository extends JpaRepository<Country, BigInteger> {

    @Query(value = "SELECT name, id, nationality, created_at, updated_at FROM country", nativeQuery = true)
    List<Country> getCountry();


    Country findByName(String name);
}
