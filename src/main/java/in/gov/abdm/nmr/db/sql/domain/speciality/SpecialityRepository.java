package in.gov.abdm.nmr.db.sql.domain.speciality;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpecialityRepository extends JpaRepository<Speciality, BigInteger> {

    @Query(value = "SELECT name, id FROM speciality", nativeQuery = true)
    List<Speciality> getSpeciality();

}
